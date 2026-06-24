package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.AmbulancePartnerRequest;
import com.vaidyo.vaidyo_backend.dto.AmbulancePartnerResponse;
import com.vaidyo.vaidyo_backend.service.AmbulanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ambulance-partners")
public class AmbulancePartnerAdminController {

    private final AmbulanceService ambulanceService;

    public AmbulancePartnerAdminController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @PostMapping
    public ResponseEntity<AmbulancePartnerResponse> createPartner(
            @Valid @RequestBody AmbulancePartnerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ambulanceService.createPartner(request));
    }

    @GetMapping
    public ResponseEntity<List<AmbulancePartnerResponse>> getAllPartners() {
        return ResponseEntity.ok(ambulanceService.getAllPartners());
    }

    @PutMapping("/{partnerId}")
    public ResponseEntity<AmbulancePartnerResponse> updatePartner(
            @PathVariable Long partnerId,
            @Valid @RequestBody AmbulancePartnerRequest request) {
        return ResponseEntity.ok(ambulanceService.updatePartner(partnerId, request));
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Map<String, String>> deletePartner(@PathVariable Long partnerId) {
        ambulanceService.deletePartner(partnerId);
        return ResponseEntity.ok(Map.of("message", "Ambulance partner deleted successfully"));
    }
}