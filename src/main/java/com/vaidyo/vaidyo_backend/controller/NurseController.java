package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.NurseProfileRequest;
import com.vaidyo.vaidyo_backend.dto.NurseProfileResponse;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import com.vaidyo.vaidyo_backend.service.NurseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class NurseController {

    private final NurseService nurseService;
    private final UserRepository userRepository;

    public NurseController(NurseService nurseService,
                           UserRepository userRepository) {
        this.nurseService = nurseService;
        this.userRepository = userRepository;
    }

    // ── POST /api/nurse/profile — nurse submits profile for verification ──
    @PostMapping("/api/nurse/profile")
    public ResponseEntity<?> submitProfile(
            @RequestBody NurseProfileRequest request,
            Authentication authentication) {
        try {
            Long userId = extractUserId(authentication);
            NurseProfileResponse response = nurseService.submitProfile(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/nurse/profile/me — nurse views own status ──
    @GetMapping("/api/nurse/profile/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        try {
            Long userId = extractUserId(authentication);
            NurseProfileResponse response = nurseService.getMyProfile(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/patient/nurses — list approved nurses for booking ──
    @GetMapping("/api/patient/nurses")
    public ResponseEntity<?> getApprovedNurses() {
        try {
            List<NurseProfileResponse> nurses = nurseService.getApprovedNurses();
            return ResponseEntity.ok(nurses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── Helper: principal is UserDetails (mobile number as username) ──
    // We look up the User entity by mobile number to get the actual userId.
    private Long extractUserId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String mobileNumber = userDetails.getUsername();

        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return user.getId();
    }
}