package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.AdminLoginRequest;
import com.vaidyo.vaidyo_backend.dto.AdminLoginResponse;
import com.vaidyo.vaidyo_backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminService adminService;

    public AdminAuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── POST /api/admin/auth/login ─────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AdminLoginRequest request) {
        try {
            AdminLoginResponse response = adminService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // ── GET /api/admin/auth/health ─────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(
                "{\"status\": \"Admin auth service is running\"}");
    }
}