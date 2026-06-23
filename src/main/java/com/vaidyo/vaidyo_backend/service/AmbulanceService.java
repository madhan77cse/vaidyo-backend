package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.AmbulancePartnerRequest;
import com.vaidyo.vaidyo_backend.dto.AmbulancePartnerResponse;
import com.vaidyo.vaidyo_backend.dto.AmbulanceRequestCreate;
import com.vaidyo.vaidyo_backend.dto.AmbulanceRequestResponse;
import com.vaidyo.vaidyo_backend.entity.AmbulancePartner;
import com.vaidyo.vaidyo_backend.entity.AmbulanceRequest;
import com.vaidyo.vaidyo_backend.entity.AmbulanceStatus;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.AmbulancePartnerRepository;
import com.vaidyo.vaidyo_backend.repository.AmbulanceRequestRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AmbulanceService {

    private final AmbulanceRequestRepository ambulanceRequestRepository;
    private final AmbulancePartnerRepository ambulancePartnerRepository;
    private final UserRepository userRepository;
    private final TelegramService telegramService;

    // Legal manual transitions a patient is allowed to trigger from a given status
    private static final Map<AmbulanceStatus, Set<AmbulanceStatus>> ALLOWED_TRANSITIONS = Map.of(
            AmbulanceStatus.REQUESTED, EnumSet.of(AmbulanceStatus.CANCELLED),
            AmbulanceStatus.ASSIGNED, EnumSet.of(AmbulanceStatus.ARRIVED, AmbulanceStatus.CANCELLED),
            AmbulanceStatus.ARRIVED, EnumSet.of(AmbulanceStatus.COMPLETED)
    );

    public AmbulanceService(AmbulanceRequestRepository ambulanceRequestRepository,
                            AmbulancePartnerRepository ambulancePartnerRepository,
                            UserRepository userRepository,
                            TelegramService telegramService) {
        this.ambulanceRequestRepository = ambulanceRequestRepository;
        this.ambulancePartnerRepository = ambulancePartnerRepository;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    private User getCurrentPatient(Authentication authentication) {
        String mobileNumber = authentication.getName();
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── Patient-facing ──────────────────────────────────────────

    @Transactional
    public AmbulanceRequestResponse requestAmbulance(Authentication authentication, AmbulanceRequestCreate request) {
        User patient = getCurrentPatient(authentication);

        AmbulanceRequest ambulanceRequest = new AmbulanceRequest();
        ambulanceRequest.setPatient(patient);
        ambulanceRequest.setPickupAddress(request.getPickupAddress());
        ambulanceRequest.setPickupLatitude(request.getPickupLatitude());
        ambulanceRequest.setPickupLongitude(request.getPickupLongitude());
        ambulanceRequest.setNotes(request.getNotes());
        ambulanceRequest.setStatus(AmbulanceStatus.REQUESTED);

        tryAutoAssign(ambulanceRequest, patient);

        AmbulanceRequest saved = ambulanceRequestRepository.save(ambulanceRequest);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AmbulanceRequestResponse> getMyRequests(Authentication authentication) {
        User patient = getCurrentPatient(authentication);
        return ambulanceRequestRepository.findByPatient_IdOrderByRequestedAtDesc(patient.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AmbulanceRequestResponse getMyRequest(Authentication authentication, Long requestId) {
        User patient = getCurrentPatient(authentication);
        AmbulanceRequest ambulanceRequest = ambulanceRequestRepository
                .findByIdAndPatient_Id(requestId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Ambulance request not found"));
        return toResponse(ambulanceRequest);
    }

    @Transactional
    public AmbulanceRequestResponse updateStatus(Authentication authentication, Long requestId,
                                                 AmbulanceStatus newStatus) {
        User patient = getCurrentPatient(authentication);
        AmbulanceRequest ambulanceRequest = ambulanceRequestRepository
                .findByIdAndPatient_Id(requestId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Ambulance request not found"));

        AmbulanceStatus currentStatus = ambulanceRequest.getStatus();
        Set<AmbulanceStatus> allowedNext = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());

        if (!allowedNext.contains(newStatus)) {
            throw new IllegalStateException(
                    "Cannot move ambulance request from " + currentStatus + " to " + newStatus);
        }

        ambulanceRequest.setStatus(newStatus);

        if (newStatus == AmbulanceStatus.COMPLETED || newStatus == AmbulanceStatus.CANCELLED) {
            ambulanceRequest.setCompletedAt(LocalDateTime.now());
            freePartner(ambulanceRequest);

            if (newStatus == AmbulanceStatus.CANCELLED && ambulanceRequest.getAssignedPartner() != null) {
                notifyPartnerOfCancellation(ambulanceRequest);
            }
        }

        AmbulanceRequest saved = ambulanceRequestRepository.save(ambulanceRequest);
        return toResponse(saved);
    }

    // ── Admin-facing (ambulance partner registry) ───────────────

    @Transactional
    public AmbulancePartnerResponse createPartner(AmbulancePartnerRequest request) {
        AmbulancePartner partner = new AmbulancePartner();
        applyPartnerFields(partner, request);
        AmbulancePartner saved = ambulancePartnerRepository.save(partner);
        return toPartnerResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AmbulancePartnerResponse> getAllPartners() {
        return ambulancePartnerRepository.findAll()
                .stream()
                .map(this::toPartnerResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AmbulancePartnerResponse updatePartner(Long partnerId, AmbulancePartnerRequest request) {
        AmbulancePartner partner = ambulancePartnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Ambulance partner not found"));
        applyPartnerFields(partner, request);
        AmbulancePartner saved = ambulancePartnerRepository.save(partner);
        return toPartnerResponse(saved);
    }

    @Transactional
    public void deletePartner(Long partnerId) {
        AmbulancePartner partner = ambulancePartnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Ambulance partner not found"));
        ambulancePartnerRepository.delete(partner);
    }

    // ── Internal helpers ─────────────────────────────────────────

    private void tryAutoAssign(AmbulanceRequest ambulanceRequest, User patient) {
        ambulancePartnerRepository.findFirstByAvailableTrueOrderByIdAsc().ifPresent(partner -> {
            partner.setAvailable(false);
            ambulancePartnerRepository.save(partner);

            ambulanceRequest.setAssignedPartner(partner);
            ambulanceRequest.setStatus(AmbulanceStatus.ASSIGNED);

            if (partner.getTelegramChatId() != null && !partner.getTelegramChatId().isBlank()) {
                String locationLink = buildLocationLink(
                        ambulanceRequest.getPickupLatitude(), ambulanceRequest.getPickupLongitude());
                String message = "🚑 <b>New Ambulance Request</b>\n\n"
                        + "Patient: <b>" + patient.getFullName() + "</b>\n"
                        + "Pickup: " + ambulanceRequest.getPickupAddress() + "\n"
                        + "📍 Location: " + locationLink
                        + (ambulanceRequest.getNotes() != null ? "\n\nNote: " + ambulanceRequest.getNotes() : "");
                telegramService.sendMessage(partner.getTelegramChatId(), message);
            }
        });
    }

    private void freePartner(AmbulanceRequest ambulanceRequest) {
        AmbulancePartner partner = ambulanceRequest.getAssignedPartner();
        if (partner != null) {
            partner.setAvailable(true);
            ambulancePartnerRepository.save(partner);
        }
    }

    private void notifyPartnerOfCancellation(AmbulanceRequest ambulanceRequest) {
        AmbulancePartner partner = ambulanceRequest.getAssignedPartner();
        if (partner != null && partner.getTelegramChatId() != null && !partner.getTelegramChatId().isBlank()) {
            String message = "❌ <b>Ambulance Request Cancelled</b>\n\n"
                    + "The request for pickup at " + ambulanceRequest.getPickupAddress()
                    + " has been cancelled by the patient.";
            telegramService.sendMessage(partner.getTelegramChatId(), message);
        }
    }

    private void applyPartnerFields(AmbulancePartner partner, AmbulancePartnerRequest request) {
        partner.setPartnerName(request.getPartnerName());
        partner.setPhoneNumber(request.getPhoneNumber());
        partner.setVehicleNumber(request.getVehicleNumber());
        partner.setTelegramChatId(request.getTelegramChatId());
        partner.setBaseLatitude(request.getBaseLatitude());
        partner.setBaseLongitude(request.getBaseLongitude());
        if (request.getAvailable() != null) {
            partner.setAvailable(request.getAvailable());
        }
    }

    private String buildLocationLink(Double latitude, Double longitude) {
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }

    private AmbulanceRequestResponse toResponse(AmbulanceRequest r) {
        AmbulancePartner partner = r.getAssignedPartner();
        return new AmbulanceRequestResponse(
                r.getId(),
                r.getPickupAddress(),
                r.getPickupLatitude(),
                r.getPickupLongitude(),
                r.getNotes(),
                r.getStatus(),
                partner != null ? partner.getPartnerName() : null,
                partner != null ? partner.getPhoneNumber() : null,
                partner != null ? partner.getVehicleNumber() : null,
                r.getRequestedAt(),
                r.getUpdatedAt(),
                r.getCompletedAt()
        );
    }

    private AmbulancePartnerResponse toPartnerResponse(AmbulancePartner p) {
        return new AmbulancePartnerResponse(
                p.getId(), p.getPartnerName(), p.getPhoneNumber(), p.getVehicleNumber(),
                p.isAvailable(), p.getBaseLatitude(), p.getBaseLongitude()
        );
    }
}