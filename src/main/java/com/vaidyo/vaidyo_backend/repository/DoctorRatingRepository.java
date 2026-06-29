package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.DoctorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRatingRepository extends JpaRepository<DoctorRating, Long> {

    List<DoctorRating> findByDoctorProfileId(Long doctorProfileId);

    // Check if rating already exists for this appointment
    Optional<DoctorRating> findByAppointmentId(Long appointmentId);

    // Compute average stars for a doctor
    @Query("SELECT AVG(r.stars) FROM DoctorRating r WHERE r.doctorProfile.id = :doctorProfileId")
    Double findAverageStarsByDoctorProfileId(@Param("doctorProfileId") Long doctorProfileId);

    // Count total ratings for a doctor
    Long countByDoctorProfileId(Long doctorProfileId);
}
