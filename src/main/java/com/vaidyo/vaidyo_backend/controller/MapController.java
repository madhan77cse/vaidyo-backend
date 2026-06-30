package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.DoctorLocationRequest;
import com.vaidyo.vaidyo_backend.dto.NearbyAmbulancePartnerResponse;
import com.vaidyo.vaidyo_backend.dto.NearbyDoctorResponse;
import com.vaidyo.vaidyo_backend.service.MapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    // Doctor sets/updates their own clinic location
    @PutMapping("/api/doctor/location")
    public ResponseEntity<Map<String, String>> setMyLocation(
            Authentication authentication,
            @Valid @RequestBody DoctorLocationRequest request) {
        mapService.setMyLocation(authentication, request);
        return ResponseEntity.ok(Map.of("message", "Location updated successfully"));
    }

    // Patient browses nearby doctors
    @GetMapping("/api/patient/nearby-doctors")
    public ResponseEntity<List<NearbyDoctorResponse>> getNearbyDoctors(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm,
            @RequestParam(required = false) String speciality) {
        return ResponseEntity.ok(
                mapService.findNearbyDoctors(latitude, longitude, radiusKm, speciality));
    }

    // Patient browses nearby ambulance partners
    @GetMapping("/api/patient/nearby-ambulance-partners")
    public ResponseEntity<List<NearbyAmbulancePartnerResponse>> getNearbyAmbulancePartners(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm) {
        return ResponseEntity.ok(
                mapService.findNearbyAmbulancePartners(latitude, longitude, radiusKm));
    }
}