package com.vaidyo.vaidyo_backend.repository;

import com.vaidyo.vaidyo_backend.entity.MedicineInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineInteractionRepository
        extends JpaRepository<MedicineInteraction, Long> {

    @Query("SELECT i FROM MedicineInteraction i WHERE " +
            "(LOWER(i.medicineA) = LOWER(:a) AND LOWER(i.medicineB) = LOWER(:b)) OR " +
            "(LOWER(i.medicineA) = LOWER(:b) AND LOWER(i.medicineB) = LOWER(:a))")
    List<MedicineInteraction> findInteraction(
            @Param("a") String a, @Param("b") String b);
}