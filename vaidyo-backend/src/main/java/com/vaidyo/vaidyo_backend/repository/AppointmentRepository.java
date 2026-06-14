package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientIdAndStatus(
            Long patientId,
            Appointment.AppointmentStatus status);

    List<Appointment> findByDoctorIdAndStatus(
            Long doctorId,
            Appointment.AppointmentStatus status);
}