package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.DoctorSearchResponse;
import com.vaidyo.vaidyo_backend.entity.DoctorAvailability;
import com.vaidyo.vaidyo_backend.entity.DoctorProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.DoctorAvailabilityRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final UserRepository userRepository;

    public DoctorService(
            DoctorProfileRepository doctorProfileRepository,
            DoctorAvailabilityRepository doctorAvailabilityRepository,
            UserRepository userRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.userRepository = userRepository;
    }

    // ── Search doctors by speciality ───────────────────────────
    @Transactional
    public List<DoctorSearchResponse> searchDoctors(
            String speciality) {

        List<DoctorProfile> profiles;

        if (speciality != null && !speciality.isEmpty()) {
            DoctorProfile.Speciality spec =
                    DoctorProfile.Speciality.valueOf(
                            speciality.toUpperCase());
            profiles = doctorProfileRepository.findAll()
                    .stream()
                    .filter(p -> p.getSpeciality() == spec)
                    .collect(Collectors.toList());
        } else {
            profiles = doctorProfileRepository.findAll();
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
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));
        return mapToSearchResponse(profile);
    }

    // ── Set doctor availability ────────────────────────────────
    @Transactional
    public DoctorAvailability setAvailability(
            Long doctorId,
            String date,
            String startTime,
            String endTime) {

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        DoctorAvailability availability =
                new DoctorAvailability();
        availability.setDoctor(doctor);
        availability.setAvailableDate(LocalDate.parse(date));
        availability.setStartTime(LocalTime.parse(startTime));
        availability.setEndTime(LocalTime.parse(endTime));
        availability.setIsBooked(false);

        return doctorAvailabilityRepository.save(availability);
    }

    // ── Get doctor availability ────────────────────────────────
    @Transactional
    public List<DoctorAvailability> getAvailability(
            Long doctorId) {
        return doctorAvailabilityRepository
                .findByDoctorIdAndIsBooked(doctorId, false);
    }

    // ── Map to response ────────────────────────────────────────
    private DoctorSearchResponse mapToSearchResponse(
            DoctorProfile profile) {
        DoctorSearchResponse response =
                new DoctorSearchResponse();
        response.setUserId(profile.getUser().getId());
        response.setFullName(profile.getUser().getFullName());
        response.setSpeciality(
                profile.getSpeciality().name());
        response.setLicenseNumber(profile.getLicenseNumber());
        response.setExperienceYears(
                profile.getExperienceYears());
        response.setConsultationFee(
                profile.getConsultationFee());
        response.setClinicAddress(profile.getClinicAddress());
        response.setBio(profile.getBio());
        response.setVerificationStatus(
                profile.getVerificationStatus().name());
        return response;
    }
}