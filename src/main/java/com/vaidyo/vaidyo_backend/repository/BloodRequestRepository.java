package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.BloodRequest;
import com.vaidyo.vaidyo_backend.entity.BloodRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByStatusOrderByCreatedAtDesc(BloodRequestStatus status);

    List<BloodRequest> findByRequestedBy_IdOrderByCreatedAtDesc(Long userId);

    Optional<BloodRequest> findByIdAndRequestedBy_Id(Long id, Long userId);
}