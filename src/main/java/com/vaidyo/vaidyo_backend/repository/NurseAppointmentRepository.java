package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.NurseAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseAppointmentRepository extends JpaRepository<NurseAppointment, Long> {

    List<NurseAppointment> findByPatientId(Long patientId);

    List<NurseAppointment> findByNurseId(Long nurseId);
}