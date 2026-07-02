package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.LanguageUpdateRequest;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import com.vaidyo.vaidyo_backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/patient")
public class LanguageController {

    private final UserRepository userRepository;
    private final MessageService messageService;

    public LanguageController(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @PutMapping("/language")
    public ResponseEntity<?> updateLanguage(
            Authentication authentication,
            @Valid @RequestBody LanguageUpdateRequest request) {

        if (!messageService.isSupported(request.getLanguage())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Unsupported language. Supported codes: en, ta, hi, te, kn, ml, mr, bn, gu, pa, or, ur"
            ));
        }

        String mobileNumber = authentication.getName();
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPreferredLanguage(request.getLanguage().toLowerCase());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Language updated successfully",
                "language", request.getLanguage().toLowerCase()
        ));
    }

    @GetMapping("/language")
    public ResponseEntity<?> getLanguage(Authentication authentication) {
        String mobileNumber = authentication.getName();
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "language", user.getPreferredLanguage() != null ? user.getPreferredLanguage() : "en",
                "supportedLanguages", Set.of("en", "ta", "hi", "te", "kn", "ml", "mr", "bn", "gu", "pa", "or", "ur")
        ));
    }
}