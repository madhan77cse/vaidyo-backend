package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.DoctorRatingRequest;
import com.vaidyo.vaidyo_backend.dto.DoctorRatingResponse;
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

    // ── GET /api/patient/doctors — search + filter ─────────────
    // All params optional. Examples:
    // /api/patient/doctors
    // /api/patient/doctors?speciality=CARDIOLOGY
    // /api/patient/doctors?minFee=200&maxFee=800&minRating=4.0
    // /api/patient/doctors?minExperience=5&speciality=GENERAL
    @GetMapping("/api/patient/doctors")
    public ResponseEntity<?> searchDoctors(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(required = false) Double minRating) {
        try {
            List<DoctorSearchResponse> doctors = doctorService.searchDoctors(
                    speciality, minExperience, maxExperience, minFee, maxFee, minRating);
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

    // ── GET /api/patient/doctors/{id}/ratings ──────────────────
    @GetMapping("/api/patient/doctors/{id}/ratings")
    public ResponseEntity<?> getDoctorRatings(@PathVariable Long id) {
        try {
            List<DoctorRatingResponse> ratings = doctorService.getDoctorRatings(id);
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/patient/doctors/rate ─────────────────────────
    @PostMapping("/api/patient/doctors/rate")
    public ResponseEntity<?> submitRating(
            @RequestBody DoctorRatingRequest request) {
        try {
            DoctorRatingResponse response = doctorService.submitRating(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/doctor/profile/photo ─────────────────────────
    @PostMapping("/api/doctor/profile/photo")
    public ResponseEntity<?> uploadProfilePhoto(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = doctorService.uploadProfilePhoto(userId, file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/doctor/certificate (already existed) ─────────
    @PostMapping("/api/doctor/certificate")
    public ResponseEntity<?> uploadCertificate(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = doctorService.uploadCertificate(userId, file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── POST /api/doctor/availability ──────────────────────────
    @PostMapping("/api/doctor/availability")
    public ResponseEntity<?> setAvailability(
            @RequestParam Long doctorId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            DoctorAvailability availability = doctorService.setAvailability(
                    doctorId, date, startTime, endTime);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/doctor/availability ───────────────────────────
    @GetMapping("/api/doctor/availability")
    public ResponseEntity<?> getAvailability(@RequestParam Long doctorId) {
        try {
            List<DoctorAvailability> availability = doctorService.getAvailability(doctorId);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}