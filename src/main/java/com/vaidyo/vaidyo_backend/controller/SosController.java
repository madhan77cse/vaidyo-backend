package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.SosRequest;
import com.vaidyo.vaidyo_backend.dto.SosResponse;
import com.vaidyo.vaidyo_backend.service.SosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient/sos")
public class SosController {

    private final SosService sosService;

    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<SosResponse> triggerSos(
            Authentication authentication,
            @Valid @RequestBody SosRequest request) {
        return ResponseEntity.ok(sosService.triggerSos(authentication, request));
    }
}