package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.*;
import com.vaidyo.vaidyo_backend.service.BloodDonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class BloodDonationController {

    private final BloodDonationService bloodDonationService;

    public BloodDonationController(BloodDonationService bloodDonationService) {
        this.bloodDonationService = bloodDonationService;
    }

    // ── Donor profile ────────────────────────────────────────────

    @PostMapping("/blood-donor/profile")
    public ResponseEntity<BloodDonorProfileResponse> upsertProfile(
            Authentication authentication,
            @Valid @RequestBody BloodDonorProfileRequest request) {
        return ResponseEntity.ok(bloodDonationService.upsertDonorProfile(authentication, request));
    }

    @GetMapping("/blood-donor/profile")
    public ResponseEntity<BloodDonorProfileResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(bloodDonationService.getMyDonorProfile(authentication));
    }

    @PutMapping("/blood-donor/availability")
    public ResponseEntity<BloodDonorProfileResponse> setAvailability(
            Authentication authentication,
            @RequestBody Map<String, Boolean> body) {
        boolean available = Boolean.TRUE.equals(body.get("available"));
        return ResponseEntity.ok(bloodDonationService.setAvailability(authentication, available));
    }

    // ── Donor search ─────────────────────────────────────────────

    @GetMapping("/blood-donor/search")
    public ResponseEntity<List<BloodDonorProfileResponse>> searchDonors(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm) {
        return ResponseEntity.ok(
                bloodDonationService.searchDonors(bloodGroup, latitude, longitude, radiusKm));
    }

    // ── Blood requests (needs) ───────────────────────────────────

    @PostMapping("/blood-requests")
    public ResponseEntity<BloodRequestResponse> createRequest(
            Authentication authentication,
            @Valid @RequestBody BloodRequestCreate request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bloodDonationService.createRequest(authentication, request));
    }

    @GetMapping("/blood-requests")
    public ResponseEntity<List<BloodRequestResponse>> getOpenRequests() {
        return ResponseEntity.ok(bloodDonationService.getOpenRequests());
    }

    @GetMapping("/blood-requests/mine")
    public ResponseEntity<List<BloodRequestResponse>> getMyRequests(Authentication authentication) {
        return ResponseEntity.ok(bloodDonationService.getMyRequests(authentication));
    }

    @PutMapping("/blood-requests/{requestId}/status")
    public ResponseEntity<BloodRequestResponse> updateRequestStatus(
            Authentication authentication,
            @PathVariable Long requestId,
            @Valid @RequestBody BloodRequestStatusUpdate request) {
        return ResponseEntity.ok(
                bloodDonationService.updateRequestStatus(authentication, requestId, request.getStatus()));
    }
}