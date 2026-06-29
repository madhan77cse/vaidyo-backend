package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.DoctorRatingRequest;
import com.vaidyo.vaidyo_backend.dto.DoctorRatingResponse;
import com.vaidyo.vaidyo_backend.dto.DoctorSearchResponse;
import com.vaidyo.vaidyo_backend.dto.DoctorVerificationResponse;
import com.vaidyo.vaidyo_backend.entity.*;
import com.vaidyo.vaidyo_backend.repository.*;
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
    private final DoctorRatingRepository doctorRatingRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(
            DoctorProfileRepository doctorProfileRepository,
            DoctorAvailabilityRepository doctorAvailabilityRepository,
            UserRepository userRepository,
            CloudinaryService cloudinaryService,
            DoctorRatingRepository doctorRatingRepository,
            AppointmentRepository appointmentRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.doctorRatingRepository = doctorRatingRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ── Search VERIFIED doctors — with optional filters ────────
    @Transactional
    public List<DoctorSearchResponse> searchDoctors(
            String speciality,
            Integer minExperience,
            Integer maxExperience,
            Double minFee,
            Double maxFee,
            Double minRating) {

        List<DoctorProfile> profiles = doctorProfileRepository.findAll()
                .stream()
                .filter(p -> p.getVerificationStatus() == DoctorProfile.VerificationStatus.VERIFIED)
                .filter(p -> speciality == null || speciality.isEmpty()
                        || p.getSpeciality() == DoctorProfile.Speciality.valueOf(speciality.toUpperCase()))
                .filter(p -> minExperience == null
                        || (p.getExperienceYears() != null && p.getExperienceYears() >= minExperience))
                .filter(p -> maxExperience == null
                        || (p.getExperienceYears() != null && p.getExperienceYears() <= maxExperience))
                .filter(p -> minFee == null
                        || (p.getConsultationFee() != null && p.getConsultationFee() >= minFee))
                .filter(p -> maxFee == null
                        || (p.getConsultationFee() != null && p.getConsultationFee() <= maxFee))
                .collect(Collectors.toList());

        // Apply rating filter after computing averages
        return profiles.stream()
                .map(this::mapToSearchResponse)
                .filter(r -> minRating == null
                        || (r.getAverageRating() != null && r.getAverageRating() >= minRating))
                .collect(Collectors.toList());
    }

    // ── Get single doctor by userId ────────────────────────────
    @Transactional
    public DoctorSearchResponse getDoctorById(Long userId) {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToSearchResponse(profile);
    }

    // ── Upload profile photo to Cloudinary ─────────────────────
    @Transactional
    public String uploadProfilePhoto(Long userId, MultipartFile file) throws IOException {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        String url = cloudinaryService.uploadFile(file, "vaidyo/doctor-photos");
        profile.setProfilePhotoUrl(url);
        doctorProfileRepository.save(profile);
        return url;
    }

    // ── Upload certificate (already existed, kept as-is) ───────
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

    // ── Submit rating (patient → doctor) ───────────────────────
    @Transactional
    public DoctorRatingResponse submitRating(DoctorRatingRequest request) {

        // 1. Validate stars range
        if (request.getStars() < 1 || request.getStars() > 5) {
            throw new RuntimeException("Stars must be between 1 and 5");
        }

        // 2. Load the appointment and verify it is COMPLETED
        Appointment appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED) {
            throw new RuntimeException("You can only rate a completed appointment");
        }

        // 3. Verify the patient owns this appointment
        if (!appointment.getPatient().getId().equals(request.getPatientId())) {
            throw new RuntimeException("This appointment does not belong to this patient");
        }

        // 4. Check rating doesn't already exist for this appointment
        if (doctorRatingRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new RuntimeException("You have already rated this appointment");
        }

        // 5. Load doctor profile
        DoctorProfile doctorProfile = doctorProfileRepository
                .findByUserId(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        // 6. Load patient
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 7. Save rating
        DoctorRating rating = new DoctorRating();
        rating.setDoctorProfile(doctorProfile);
        rating.setPatient(patient);
        rating.setAppointment(appointment);
        rating.setStars(request.getStars());
        rating.setComment(request.getComment());

        DoctorRating saved = doctorRatingRepository.save(rating);
        return new DoctorRatingResponse(saved);
    }

    // ── Get all ratings for a doctor ───────────────────────────
    @Transactional
    public List<DoctorRatingResponse> getDoctorRatings(Long userId) {
        DoctorProfile profile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return doctorRatingRepository
                .findByDoctorProfileId(profile.getId())
                .stream()
                .map(DoctorRatingResponse::new)
                .collect(Collectors.toList());
    }

    // ── Admin methods (unchanged) ──────────────────────────────
    @Transactional
    public List<DoctorVerificationResponse> getPendingDoctors() {
        return doctorProfileRepository.findAll()
                .stream()
                .filter(p -> p.getVerificationStatus() == DoctorProfile.VerificationStatus.PENDING)
                .map(this::mapToVerificationResponse)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public List<DoctorAvailability> getAvailability(Long doctorId) {
        return doctorAvailabilityRepository
                .findByDoctorIdAndIsBooked(doctorId, false);
    }

    // ── Map to search response (extended with rating + new fields) ──
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
        // NEW
        response.setQualifications(profile.getQualifications());
        response.setProfilePhotoUrl(profile.getProfilePhotoUrl());
        response.setAverageRating(
                doctorRatingRepository.findAverageStarsByDoctorProfileId(profile.getId()));
        response.setTotalRatings(
                doctorRatingRepository.countByDoctorProfileId(profile.getId()));
        return response;
    }

    private DoctorVerificationResponse mapToVerificationResponse(DoctorProfile profile) {
        return DoctorVerificationResponse.from(profile);
    }
}