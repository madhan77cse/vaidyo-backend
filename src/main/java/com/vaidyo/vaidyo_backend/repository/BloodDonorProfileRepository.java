package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.BloodDonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodDonorProfileRepository extends JpaRepository<BloodDonorProfile, Long> {

    Optional<BloodDonorProfile> findByUser_Id(Long userId);

    List<BloodDonorProfile> findByAvailableTrue();

    List<BloodDonorProfile> findByAvailableTrueAndBloodGroup(String bloodGroup);
}