package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.VideoVisitResponse;
import com.vaidyo.vaidyo_backend.service.VideoVisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VideoVisitController {

    private final VideoVisitService videoVisitService;

    public VideoVisitController(VideoVisitService videoVisitService) {
        this.videoVisitService = videoVisitService;
    }

    // Doctor marks an appointment as an online visit and generates (or fetches) the room link
    @PutMapping("/api/doctor/appointments/{appointmentId}/video-visit")
    public ResponseEntity<?> enableVideoVisit(@PathVariable Long appointmentId) {
        try {
            VideoVisitResponse response = videoVisitService.createOrGetVideoVisit(appointmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Doctor fetches the room link for an appointment
    @GetMapping("/api/doctor/appointments/{appointmentId}/video-visit")
    public ResponseEntity<?> getVideoVisitForDoctor(@PathVariable Long appointmentId) {
        try {
            return ResponseEntity.ok(videoVisitService.getVideoVisit(appointmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Patient fetches the room link for an appointment
    @GetMapping("/api/patient/appointments/{appointmentId}/video-visit")
    public ResponseEntity<?> getVideoVisitForPatient(@PathVariable Long appointmentId) {
        try {
            return ResponseEntity.ok(videoVisitService.getVideoVisit(appointmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}