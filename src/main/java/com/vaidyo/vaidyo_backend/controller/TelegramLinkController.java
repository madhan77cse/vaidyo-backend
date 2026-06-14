package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.service.TelegramLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
@CrossOrigin(origins = "*")
public class TelegramLinkController {

    private final TelegramLinkService telegramLinkService;

    public TelegramLinkController(
            TelegramLinkService telegramLinkService) {
        this.telegramLinkService = telegramLinkService;
    }

    // ── GET /api/telegram/generate/{userId} ────────────────────
    // Frontend calls this to get a link code
    @GetMapping("/generate/{userId}")
    public ResponseEntity<?> generateCode(
            @PathVariable Long userId) {
        try {
            String code =
                    telegramLinkService.generateLinkCode(userId);

            Map<String, String> response = new HashMap<>();
            response.put("code", code);
            response.put("message",
                    "Send this code to @vaidyo_health_bot"
                            + " on Telegram");
            response.put("botUsername", "@vaidyo_health_bot");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // ── POST /api/telegram/verify ──────────────────────────────
    // Telegram bot calls this when user sends code
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(
            @RequestParam String code,
            @RequestParam String chatId) {
        try {
            String result =
                    telegramLinkService.verifyAndLink(
                            code, chatId);

            if (result.equals("SUCCESS")) {
                return ResponseEntity.ok(
                        "Telegram linked successfully!");
            } else {
                return ResponseEntity.badRequest()
                        .body("Invalid or expired code!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // ── GET /api/telegram/status/{userId} ─────────────────────
    // Check if user has linked Telegram
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> checkStatus(
            @PathVariable Long userId) {
        try {
            Map<String, Object> response = new HashMap<>();

            com.vaidyo.vaidyo_backend.entity.User user =
                    telegramLinkService.getUserById(userId);

            boolean isLinked =
                    user.getTelegramChatId() != null;
            response.put("linked", isLinked);
            response.put("message", isLinked
                    ? "Telegram is linked ✅"
                    : "Telegram not linked yet");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}