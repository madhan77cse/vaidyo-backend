package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.VideoVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoVisitRepository extends JpaRepository<VideoVisit, Long> {

    Optional<VideoVisit> findByAppointment_Id(Long appointmentId);
}