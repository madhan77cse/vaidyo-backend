package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.AmbulanceRequestCreate;
import com.vaidyo.vaidyo_backend.dto.AmbulanceRequestResponse;
import com.vaidyo.vaidyo_backend.dto.AmbulanceStatusUpdateRequest;
import com.vaidyo.vaidyo_backend.service.AmbulanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient/ambulance")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @PostMapping("/request")
    public ResponseEntity<AmbulanceRequestResponse> requestAmbulance(
            Authentication authentication,
            @Valid @RequestBody AmbulanceRequestCreate request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ambulanceService.requestAmbulance(authentication, request));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<AmbulanceRequestResponse>> getMyRequests(Authentication authentication) {
        return ResponseEntity.ok(ambulanceService.getMyRequests(authentication));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<AmbulanceRequestResponse> getMyRequest(
            Authentication authentication,
            @PathVariable Long requestId) {
        return ResponseEntity.ok(ambulanceService.getMyRequest(authentication, requestId));
    }

    @PutMapping("/requests/{requestId}/status")
    public ResponseEntity<AmbulanceRequestResponse> updateStatus(
            Authentication authentication,
            @PathVariable Long requestId,
            @Valid @RequestBody AmbulanceStatusUpdateRequest request) {
        return ResponseEntity.ok(
                ambulanceService.updateStatus(authentication, requestId, request.getStatus()));
    }
}