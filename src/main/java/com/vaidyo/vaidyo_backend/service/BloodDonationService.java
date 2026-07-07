package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.*;
import com.vaidyo.vaidyo_backend.entity.*;
import com.vaidyo.vaidyo_backend.repository.BloodDonorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.BloodRequestRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import com.vaidyo.vaidyo_backend.util.GeoUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodDonationService {

    private final BloodDonorProfileRepository donorProfileRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    public BloodDonationService(BloodDonorProfileRepository donorProfileRepository,
                                BloodRequestRepository bloodRequestRepository,
                                UserRepository userRepository) {
        this.donorProfileRepository = donorProfileRepository;
        this.bloodRequestRepository = bloodRequestRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String mobileNumber = authentication.getName();
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── Donor profile ────────────────────────────────────────────

    @Transactional
    public BloodDonorProfileResponse upsertDonorProfile(Authentication authentication,
                                                        BloodDonorProfileRequest request) {
        User user = getCurrentUser(authentication);

        BloodDonorProfile profile = donorProfileRepository.findByUser_Id(user.getId())
                .orElseGet(BloodDonorProfile::new);

        profile.setUser(user);
        profile.setBloodGroup(request.getBloodGroup());
        profile.setLatitude(request.getLatitude());
        profile.setLongitude(request.getLongitude());
        profile.setLocationLabel(request.getLocationLabel());
        profile.setLastDonationDate(request.getLastDonationDate());
        profile.setPhoneNumber(
                request.getPhoneNumber() != null ? request.getPhoneNumber() : user.getMobileNumber());

        if (request.getContactPreference() != null) {
            profile.setContactPreference(ContactPreference.valueOf(request.getContactPreference().toUpperCase()));
        }
        if (request.getAvailable() != null) {
            profile.setAvailable(request.getAvailable());
        }

        BloodDonorProfile saved = donorProfileRepository.save(profile);
        return toProfileResponse(saved, user.getFullName(), null);
    }

    @Transactional(readOnly = true)
    public BloodDonorProfileResponse getMyDonorProfile(Authentication authentication) {
        User user = getCurrentUser(authentication);
        BloodDonorProfile profile = donorProfileRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException(
                        "No donor profile found. Create one first via POST /api/patient/blood-donor/profile"));
        return toProfileResponse(profile, user.getFullName(), null);
    }

    @Transactional
    public BloodDonorProfileResponse setAvailability(Authentication authentication, boolean available) {
        User user = getCurrentUser(authentication);
        BloodDonorProfile profile = donorProfileRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException(
                        "No donor profile found. Create one first via POST /api/patient/blood-donor/profile"));
        profile.setAvailable(available);
        BloodDonorProfile saved = donorProfileRepository.save(profile);
        return toProfileResponse(saved, user.getFullName(), null);
    }

    // ── Donor search ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BloodDonorProfileResponse> searchDonors(String bloodGroup, String location) {
        List<BloodDonorProfile> candidates =
                donorProfileRepository.searchDonors(
                        (bloodGroup != null && !bloodGroup.isBlank()) ? bloodGroup : null,
                        (location != null && !location.isBlank()) ? location : null
                );

        return candidates.stream()
                .map(profile -> toProfileResponse(profile, profile.getUser().getFullName(), null))
                .collect(Collectors.toList());
    }

    // ── Blood requests (needs) ───────────────────────────────────

    @Transactional
    public BloodRequestResponse createRequest(Authentication authentication, BloodRequestCreate request) {
        User requester = getCurrentUser(authentication);

        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setRequestedBy(requester);
        bloodRequest.setBloodGroupNeeded(request.getBloodGroupNeeded());
        bloodRequest.setUrgency(UrgencyLevel.valueOf(request.getUrgency().toUpperCase()));
        bloodRequest.setLatitude(request.getLatitude());
        bloodRequest.setLongitude(request.getLongitude());
        bloodRequest.setLocationLabel(request.getLocationLabel());
        bloodRequest.setNotes(request.getNotes());
        bloodRequest.setStatus(BloodRequestStatus.OPEN);

        BloodRequest saved = bloodRequestRepository.save(bloodRequest);
        return toRequestResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BloodRequestResponse> getOpenRequests() {
        return bloodRequestRepository.findByStatusOrderByCreatedAtDesc(BloodRequestStatus.OPEN)
                .stream()
                .map(this::toRequestResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BloodRequestResponse> getMyRequests(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return bloodRequestRepository.findByRequestedBy_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toRequestResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BloodRequestResponse updateRequestStatus(Authentication authentication, Long requestId, String newStatus) {
        User user = getCurrentUser(authentication);
        BloodRequest bloodRequest = bloodRequestRepository.findByIdAndRequestedBy_Id(requestId, user.getId())
                .orElseThrow(() -> new RuntimeException("Blood request not found"));

        BloodRequestStatus status = BloodRequestStatus.valueOf(newStatus.toUpperCase());

        if (bloodRequest.getStatus() != BloodRequestStatus.OPEN) {
            throw new IllegalStateException(
                    "Cannot update a request that is already " + bloodRequest.getStatus());
        }
        if (status == BloodRequestStatus.OPEN) {
            throw new IllegalStateException("Cannot set status back to OPEN");
        }

        bloodRequest.setStatus(status);
        BloodRequest saved = bloodRequestRepository.save(bloodRequest);
        return toRequestResponse(saved);
    }

    // ── Mapping helpers ──────────────────────────────────────────

    private BloodDonorProfileResponse toProfileResponse(BloodDonorProfile profile, String donorName, Double distanceKm) {
        return new BloodDonorProfileResponse(
                profile.getId(),
                donorName,
                profile.getBloodGroup(),
                profile.getLatitude(),
                profile.getLongitude(),
                profile.getLocationLabel(),
                profile.getLastDonationDate(),
                profile.getContactPreference().name(),
                profile.getPhoneNumber(),
                profile.isAvailable(),
                distanceKm,
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    private BloodRequestResponse toRequestResponse(BloodRequest r) {
        return new BloodRequestResponse(
                r.getId(),
                r.getRequestedBy().getFullName(),
                r.getBloodGroupNeeded(),
                r.getUrgency().name(),
                r.getLatitude(),
                r.getLongitude(),
                r.getLocationLabel(),
                r.getNotes(),
                r.getStatus().name(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}