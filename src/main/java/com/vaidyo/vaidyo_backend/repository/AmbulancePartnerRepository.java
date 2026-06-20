package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.AmbulancePartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmbulancePartnerRepository extends JpaRepository<AmbulancePartner, Long> {

    Optional<AmbulancePartner> findFirstByAvailableTrueOrderByIdAsc();
}