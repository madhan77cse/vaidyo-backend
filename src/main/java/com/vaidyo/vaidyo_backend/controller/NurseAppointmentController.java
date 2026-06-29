package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.NurseAppointmentRequest;
import com.vaidyo.vaidyo_backend.dto.NurseAppointmentResponse;
import com.vaidyo.vaidyo_backend.service.NurseAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class NurseAppointmentController {

    private final NurseAppointmentService nurseAppointmentService;

    public NurseAppointmentController(NurseAppointmentService nurseAppointmentService) {
        this.nurseAppointmentService = nurseAppointmentService;
    }

    // ── POST /api/patient/nurse-appointments ───────────────────
    @PostMapping("/api/patient/nurse-appointments")
    public ResponseEntity<?> bookAppointment(
            @RequestParam Long patientId,
            @RequestBody NurseAppointmentRequest request) {
        try {
            NurseAppointmentResponse response =
                    nurseAppointmentService.bookAppointment(patientId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/patient/nurse-appointments ────────────────────
    @GetMapping("/api/patient/nurse-appointments")
    public ResponseEntity<?> getPatientAppointments(
            @RequestParam Long patientId) {
        try {
            List<NurseAppointmentResponse> appointments =
                    nurseAppointmentService.getPatientAppointments(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/nurse/appointments ────────────────────────────
    @GetMapping("/api/nurse/appointments")
    public ResponseEntity<?> getNurseAppointments(
            @RequestParam Long nurseId) {
        try {
            List<NurseAppointmentResponse> appointments =
                    nurseAppointmentService.getNurseAppointments(nurseId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/nurse/appointments/{id}/reply ─────────────────
    @PutMapping("/api/nurse/appointments/{id}/reply")
    public ResponseEntity<?> nurseReply(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        try {
            String scheduledAt = (String) body.get("scheduledAt");
            Double fee = Double.parseDouble(body.get("fee").toString());
            String notes = (String) body.get("notes");
            NurseAppointmentResponse response =
                    nurseAppointmentService.nurseReply(id, scheduledAt, fee, notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/patient/nurse-appointments/{id}/accept ────────
    @PutMapping("/api/patient/nurse-appointments/{id}/accept")
    public ResponseEntity<?> acceptAppointment(@PathVariable Long id) {
        try {
            NurseAppointmentResponse response =
                    nurseAppointmentService.acceptAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/patient/nurse-appointments/{id}/reject ────────
    @PutMapping("/api/patient/nurse-appointments/{id}/reject")
    public ResponseEntity<?> rejectAppointment(@PathVariable Long id) {
        try {
            NurseAppointmentResponse response =
                    nurseAppointmentService.rejectAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── PUT /api/nurse/appointments/{id}/complete ──────────────
    @PutMapping("/api/nurse/appointments/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id) {
        try {
            NurseAppointmentResponse response =
                    nurseAppointmentService.completeAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}