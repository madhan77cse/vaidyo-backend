package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByUserId(Long userId);

    // ── New ──────────────────────────────────────────────────
    List<DoctorProfile> findByVerificationStatus(
            DoctorProfile.VerificationStatus status);

    long countByVerificationStatus(
            DoctorProfile.VerificationStatus status);
}