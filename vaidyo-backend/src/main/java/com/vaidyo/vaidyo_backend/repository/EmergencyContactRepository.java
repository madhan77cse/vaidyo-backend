package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    List<EmergencyContact> findByPatient_IdOrderByPriorityOrderAsc(Long patientId);

    Optional<EmergencyContact> findByIdAndPatient_Id(Long id, Long patientId);

    long countByPatient_Id(Long patientId);
}