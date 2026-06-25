package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.MedicineRequest;
import com.vaidyo.vaidyo_backend.entity.MedicineLog;
import com.vaidyo.vaidyo_backend.service.CloudinaryService;
import com.vaidyo.vaidyo_backend.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*")
public class MedicineController {

    private final MedicineService medicineService;
    private final CloudinaryService cloudinaryService;

    public MedicineController(MedicineService medicineService,
                              CloudinaryService cloudinaryService) {
        this.medicineService = medicineService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/medicines")
    public ResponseEntity<?> addMedicine(
            @RequestParam Long patientId,
            @RequestBody MedicineRequest request) {
        try {
            Map<String, Object> response =
                    medicineService.addMedicine(patientId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medicines")
    public ResponseEntity<?> getMyMedicines(
            @RequestParam Long patientId) {
        try {
            return ResponseEntity.ok(
                    medicineService.getMyMedicines(patientId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/medicines/{id}/taken")
    public ResponseEntity<?> markAsTaken(
            @PathVariable Long id,
            @RequestParam Long patientId) {
        try {
            String result = medicineService.markAsTaken(id, patientId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @GetMapping("/medicines/logs")
    public ResponseEntity<?> getMedicineLogs(
            @RequestParam Long patientId) {
        try {
            List<MedicineLog> logs =
                    medicineService.getMedicineLogs(patientId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/medicines/{id}")
    public ResponseEntity<?> deleteMedicine(
            @PathVariable Long id) {
        try {
            String result = medicineService.deleteMedicine(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/medicines/{id}/photo")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String photoUrl = cloudinaryService.uploadFile(
                    file, "vaidyo/medicines");
            medicineService.updatePhotoUrl(id, photoUrl);
            return ResponseEntity.ok(
                    Map.of("photoUrl", photoUrl,
                            "message", "Photo uploaded successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Upload failed: " + e.getMessage());
        }
    }
}