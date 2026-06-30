package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.DoctorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorLocationRepository extends JpaRepository<DoctorLocation, Long> {

    Optional<DoctorLocation> findByDoctorProfile_Id(Long doctorProfileId);
}