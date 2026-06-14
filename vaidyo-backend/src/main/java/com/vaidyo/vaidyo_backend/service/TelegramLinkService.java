package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TelegramLinkService {

    private final UserRepository userRepository;
    private final TelegramService telegramService;

    private final Map<String, Long> pendingCodes
            = new HashMap<>();

    public TelegramLinkService(
            UserRepository userRepository,
            TelegramService telegramService) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    public String generateLinkCode(Long userId) {
        String code = String.format("%06d",
                new Random().nextInt(999999));
        pendingCodes.put(code, userId);
        return code;
    }

    public String verifyAndLink(String code, String chatId) {
        if (!pendingCodes.containsKey(code)) {
            return "INVALID";
        }
        Long userId = pendingCodes.get(code);
        User user = userRepository.findById(userId)
                .orElse(null);
        if (user == null) {
            return "INVALID";
        }
        user.setTelegramChatId(chatId);
        userRepository.save(user);
        pendingCodes.remove(code);
        telegramService.sendWelcomeMessage(
                chatId, user.getFullName());
        return "SUCCESS";
    }

    public boolean codeExists(String code) {
        return pendingCodes.containsKey(code);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}