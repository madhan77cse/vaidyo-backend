package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorId(Long doctorId);

    List<DoctorAvailability> findByDoctorIdAndAvailableDate(
            Long doctorId, LocalDate date);

    List<DoctorAvailability> findByDoctorIdAndIsBooked(
            Long doctorId, Boolean isBooked);

    List<DoctorAvailability> findByAvailableDateGreaterThanEqual(
            LocalDate date);
}