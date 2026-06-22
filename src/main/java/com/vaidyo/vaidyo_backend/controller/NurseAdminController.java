package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.NurseProfileResponse;
import com.vaidyo.vaidyo_backend.dto.NurseRejectRequest;
import com.vaidyo.vaidyo_backend.service.NurseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This controller is what Teammate 2's admin dashboard calls into.
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin/nurses")
public class NurseAdminController {

    private final NurseService nurseService;

    public NurseAdminController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    // ── GET /api/admin/nurses/pending ──
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingNurses() {
        try {
            List<NurseProfileResponse> nurses = nurseService.getPendingNurses();
            return ResponseEntity.ok(nurses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/admin/nurses/{id}/approve ──
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveNurse(@PathVariable Long id) {
        try {
            NurseProfileResponse response = nurseService.approveNurse(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/admin/nurses/{id}/reject ──
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectNurse(@PathVariable Long id,
                                         @RequestBody NurseRejectRequest request) {
        try {
            NurseProfileResponse response = nurseService.rejectNurse(id, request.getReason());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}