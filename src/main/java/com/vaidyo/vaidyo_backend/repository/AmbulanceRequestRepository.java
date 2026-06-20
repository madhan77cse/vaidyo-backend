package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.AmbulanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AmbulanceRequestRepository extends JpaRepository<AmbulanceRequest, Long> {

    List<AmbulanceRequest> findByPatient_IdOrderByRequestedAtDesc(Long patientId);

    Optional<AmbulanceRequest> findByIdAndPatient_Id(Long id, Long patientId);
}