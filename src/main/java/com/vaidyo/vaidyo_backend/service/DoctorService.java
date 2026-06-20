package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.DoctorSearchResponse;
import com.vaidyo.vaidyo_backend.dto.DoctorVerificationResponse;
import com.vaidyo.vaidyo_backend.entity.DoctorAvailability;
import com.vaidyo.vaidyo_backend.entity.DoctorProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.DoctorAvailabilityRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public DoctorService(
            DoctorProfileRepository doctorProfileRepository,
            DoctorAvailabilityRepository doctorAvailabilityRepository,
            UserRepository userRepository,
            CloudinaryService cloudinaryService) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // ── Search VERIFIED doctors only ───────────────────────────
    @Transactional
    public List<DoctorSearchResponse> searchDoctors(String speciality) {

        List<DoctorProfile> profiles;

        if (speciality != null && !speciality.isEmpty()) {
            DoctorProfile.Speciality spec =
                    DoctorProfile.Speciality.valueOf(speciality.toUpperCase());
            profiles = doctorProfileRepository.findAll()
                    .stream()
                    .filter(p -> p.getSpeciality() == spec
                            && p.getVerificationStatus() == DoctorProfile.VerificationStatus.VERIFIED)
                    .collect(Collectors.toList());
        } else {
            profiles = doctorProfileRepository.findAll()
                    .stream()
                    .filter(p -> p.getVerificationStatus() == DoctorProfile.VerificationStatus.VERIFIED)
                    .collect(Collectors.toList());
        }

        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }

    // ── Get doctor by id ───────────────────────────────────────
    @Transactional
    public DoctorSearchResponse getDoctorById(Long userId) {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToSearchResponse(profile);
    }

    // ── Upload certificate ─────────────────────────────────────
    @Transactional
    public String uploadCertificate(Long userId, MultipartFile file) throws IOException {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        String url = cloudinaryService.uploadFile(file, "vaidyo/doctor-certificates");
        profile.setCertificateUrl(url);
        doctorProfileRepository.save(profile);
        return url;
    }

    // ── Get all PENDING doctors (for admin) ────────────────────
    @Transactional
    public List<DoctorVerificationResponse> getPendingDoctors() {
        return doctorProfileRepository.findAll()
                .stream()
                .filter(p -> p.getVerificationStatus() == DoctorProfile.VerificationStatus.PENDING)
                .map(this::mapToVerificationResponse)
                .collect(Collectors.toList());
    }

    // ── Approve doctor ─────────────────────────────────────────
    @Transactional
    public String approveDoctor(Long userId) {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        profile.setVerificationStatus(DoctorProfile.VerificationStatus.VERIFIED);
        profile.setRejectionReason(null);
        doctorProfileRepository.save(profile);
        return "Doctor approved successfully";
    }

    // ── Reject doctor ──────────────────────────────────────────
    @Transactional
    public String rejectDoctor(Long userId, String reason) {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        profile.setVerificationStatus(DoctorProfile.VerificationStatus.REJECTED);
        profile.setRejectionReason(reason);
        doctorProfileRepository.save(profile);
        return "Doctor rejected";
    }

    // ── Set doctor availability ────────────────────────────────
    @Transactional
    public DoctorAvailability setAvailability(
            Long doctorId, String date,
            String startTime, String endTime) {

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctor(doctor);
        availability.setAvailableDate(LocalDate.parse(date));
        availability.setStartTime(LocalTime.parse(startTime));
        availability.setEndTime(LocalTime.parse(endTime));
        availability.setIsBooked(false);

        return doctorAvailabilityRepository.save(availability);
    }

    // ── Get doctor availability ────────────────────────────────
    @Transactional
    public List<DoctorAvailability> getAvailability(Long doctorId) {
        return doctorAvailabilityRepository
                .findByDoctorIdAndIsBooked(doctorId, false);
    }

    // ── Map to search response ─────────────────────────────────
    private DoctorSearchResponse mapToSearchResponse(DoctorProfile profile) {
        DoctorSearchResponse response = new DoctorSearchResponse();
        response.setUserId(profile.getUser().getId());
        response.setFullName(profile.getUser().getFullName());
        response.setSpeciality(profile.getSpeciality().name());
        response.setLicenseNumber(profile.getLicenseNumber());
        response.setExperienceYears(profile.getExperienceYears());
        response.setConsultationFee(profile.getConsultationFee());
        response.setClinicAddress(profile.getClinicAddress());
        response.setBio(profile.getBio());
        response.setVerificationStatus(profile.getVerificationStatus().name());
        return response;
    }

    // ── Map to verification response ───────────────────────────
    private DoctorVerificationResponse mapToVerificationResponse(DoctorProfile profile) {
        DoctorVerificationResponse response = new DoctorVerificationResponse();
        response.setUserId(profile.getUser().getId());
        response.setFullName(profile.getUser().getFullName());
        response.setSpeciality(profile.getSpeciality().name());
        response.setLicenseNumber(profile.getLicenseNumber());
        response.setVerificationStatus(profile.getVerificationStatus().name());
        response.setCertificateUrl(profile.getCertificateUrl());
        response.setRejectionReason(profile.getRejectionReason());
        return response;
    }
}