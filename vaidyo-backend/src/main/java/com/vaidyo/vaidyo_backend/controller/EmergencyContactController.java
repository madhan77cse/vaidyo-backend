package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.EmergencyContactRequest;
import com.vaidyo.vaidyo_backend.dto.EmergencyContactResponse;
import com.vaidyo.vaidyo_backend.service.EmergencyContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    public ResponseEntity<EmergencyContactResponse> addContact(
            Authentication authentication,
            @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emergencyContactService.addContact(authentication, request));
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContactResponse>> getContacts(Authentication authentication) {
        return ResponseEntity.ok(emergencyContactService.getContacts(authentication));
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<EmergencyContactResponse> updateContact(
            Authentication authentication,
            @PathVariable Long contactId,
            @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.ok(emergencyContactService.updateContact(authentication, contactId, request));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Map<String, String>> deleteContact(
            Authentication authentication,
            @PathVariable Long contactId) {
        emergencyContactService.deleteContact(authentication, contactId);
        return ResponseEntity.ok(Map.of("message", "Emergency contact deleted successfully"));
    }
}