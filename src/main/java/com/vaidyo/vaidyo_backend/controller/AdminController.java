package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.AdminStatsResponse;
import com.vaidyo.vaidyo_backend.dto.DoctorVerificationResponse;
import com.vaidyo.vaidyo_backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── GET /api/admin/stats ───────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    // ── GET /api/admin/doctors ─────────────────────────────────
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorVerificationResponse>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    // ── GET /api/admin/doctors/pending ─────────────────────────
    @GetMapping("/doctors/pending")
    public ResponseEntity<List<DoctorVerificationResponse>> getPendingDoctors() {
        return ResponseEntity.ok(adminService.getPendingDoctors());
    }

    // ── POST /api/admin/doctors/{id}/approve ───────────────────
    @PostMapping("/doctors/{id}/approve")
    public ResponseEntity<DoctorVerificationResponse> approveDoctor(
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.approveDoctor(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── POST /api/admin/doctors/{id}/reject ────────────────────
    @PostMapping("/doctors/{id}/reject")
    public ResponseEntity<DoctorVerificationResponse> rejectDoctor(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            return ResponseEntity.ok(
                    adminService.rejectDoctor(id, reason));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}