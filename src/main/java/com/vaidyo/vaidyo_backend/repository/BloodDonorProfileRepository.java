package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.BloodDonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BloodDonorProfileRepository extends JpaRepository<BloodDonorProfile, Long> {

    Optional<BloodDonorProfile> findByUser_Id(Long userId);

    List<BloodDonorProfile> findByAvailableTrue();

    List<BloodDonorProfile> findByAvailableTrueAndBloodGroup(String bloodGroup);

    @Query("SELECT p FROM BloodDonorProfile p WHERE p.available = true " +
            "AND (:bloodGroup IS NULL OR p.bloodGroup = :bloodGroup) " +
            "AND (:location IS NULL OR LOWER(p.locationLabel) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<BloodDonorProfile> searchDonors(@Param("bloodGroup") String bloodGroup,
                                         @Param("location") String location);
}