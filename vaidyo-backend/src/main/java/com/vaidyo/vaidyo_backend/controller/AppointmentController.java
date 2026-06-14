package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.AppointmentRequest;
import com.vaidyo.vaidyo_backend.dto.AppointmentResponse;
import com.vaidyo.vaidyo_backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/api/patient/appointments")
    public ResponseEntity<?> bookAppointment(
            @RequestParam Long patientId,
            @RequestBody AppointmentRequest request) {
        try {
            AppointmentResponse response =
                    appointmentService.bookAppointment(
                            patientId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/api/patient/appointments")
    public ResponseEntity<?> getPatientAppointments(
            @RequestParam Long patientId) {
        try {
            List<AppointmentResponse> appointments =
                    appointmentService
                            .getPatientAppointments(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/api/doctor/appointments")
    public ResponseEntity<?> getDoctorAppointments(
            @RequestParam Long doctorId) {
        try {
            List<AppointmentResponse> appointments =
                    appointmentService
                            .getDoctorAppointments(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/api/doctor/appointments/{id}/reply")
    public ResponseEntity<?> doctorReply(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        try {
            String scheduledAt =
                    (String) body.get("scheduledAt");
            Double fee = Double.parseDouble(
                    body.get("fee").toString());
            String notes = (String) body.get("notes");

            AppointmentResponse response =
                    appointmentService.doctorReply(
                            id, scheduledAt, fee, notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/api/patient/appointments/{id}/accept")
    public ResponseEntity<?> acceptAppointment(
            @PathVariable Long id) {
        try {
            AppointmentResponse response =
                    appointmentService.acceptAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/api/patient/appointments/{id}/reject")
    public ResponseEntity<?> rejectAppointment(
            @PathVariable Long id) {
        try {
            AppointmentResponse response =
                    appointmentService.rejectAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/api/doctor/appointments/{id}/complete")
    public ResponseEntity<?> completeAppointment(
            @PathVariable Long id) {
        try {
            AppointmentResponse response =
                    appointmentService.completeAppointment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}