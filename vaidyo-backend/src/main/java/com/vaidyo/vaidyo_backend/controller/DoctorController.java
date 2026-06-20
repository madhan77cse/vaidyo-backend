package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.DoctorSearchResponse;
import com.vaidyo.vaidyo_backend.entity.DoctorAvailability;
import com.vaidyo.vaidyo_backend.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // ── GET /api/patient/doctors ───────────────────────────────
    @GetMapping("/api/patient/doctors")
    public ResponseEntity<?> searchDoctors(
            @RequestParam(required = false) String speciality) {
        try {
            List<DoctorSearchResponse> doctors =
                    doctorService.searchDoctors(speciality);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/patient/doctors/{id} ──────────────────────────
    @GetMapping("/api/patient/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            DoctorSearchResponse doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/doctor/certificate ───────────────────────────
    // Doctor uploads their certificate image after registration
    @PostMapping("/api/doctor/certificate")
    public ResponseEntity<?> uploadCertificate(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = doctorService.uploadCertificate(userId, file);
            return ResponseEntity.ok("Certificate uploaded: " + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/doctor/availability ─────────────────────────
    @PostMapping("/api/doctor/availability")
    public ResponseEntity<?> setAvailability(
            @RequestParam Long doctorId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            DoctorAvailability availability =
                    doctorService.setAvailability(doctorId, date, startTime, endTime);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/doctor/availability ──────────────────────────
    @GetMapping("/api/doctor/availability")
    public ResponseEntity<?> getAvailability(@RequestParam Long doctorId) {
        try {
            List<DoctorAvailability> availability =
                    doctorService.getAvailability(doctorId);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}