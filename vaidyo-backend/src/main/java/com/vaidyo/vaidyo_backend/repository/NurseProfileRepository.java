package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.NurseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseProfileRepository extends JpaRepository<NurseProfile, Long> {

    Optional<NurseProfile> findByUserId(Long userId);

    List<NurseProfile> findByVerificationStatus(NurseProfile.VerificationStatus status);

    List<NurseProfile> findByVerificationStatusOrderByCreatedAtDesc(NurseProfile.VerificationStatus status);
}