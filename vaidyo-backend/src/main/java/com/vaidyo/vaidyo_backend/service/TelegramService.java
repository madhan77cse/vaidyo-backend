package com.vaidyo.vaidyo_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private static final String TELEGRAM_API =
            "https://api.telegram.org/bot";

    // ── Send message to a Telegram chat ───────────────────────
    public void sendMessage(String chatId, String message) {
        try {
            String url = TELEGRAM_API + botToken
                    + "/sendMessage";

            RestTemplate rt = new RestTemplate();

            Map<String, String> body = new HashMap<>();
            body.put("chat_id", chatId);
            body.put("text", message);
            body.put("parse_mode", "HTML");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity =
                    new HttpEntity<>(body, headers);

            rt.postForObject(url, entity, String.class);

        } catch (Exception e) {
            System.err.println("Telegram error: "
                    + e.getMessage());
        }
    }

    // ── Send medicine reminder ─────────────────────────────────
    public void sendMedicineReminder(String chatId,
                                     String patientName,
                                     String medicineName,
                                     String dosage) {
        String message = "💊 <b>Medicine Reminder</b>\n\n"
                + "Hi " + patientName + "!\n"
                + "Time to take your medicine:\n\n"
                + "🔹 <b>" + medicineName + "</b>"
                + (dosage != null ? " - " + dosage : "")
                + "\n\nPlease confirm in the Vaidyo app "
                + "after taking it. ✅";

        sendMessage(chatId, message);
    }

    // ── Send missed medicine alert to caretaker ────────────────
    public void sendMissedAlert(String caretakerChatId,
                                String patientName,
                                String medicineName) {
        String message = "⚠️ <b>Missed Medicine Alert</b>\n\n"
                + "Your patient <b>" + patientName
                + "</b> has missed their medicine:\n\n"
                + "🔴 <b>" + medicineName + "</b>\n\n"
                + "Please check on them immediately!";

        sendMessage(caretakerChatId, message);
    }

    // ── Send welcome message when user links Telegram ──────────
    public void sendWelcomeMessage(String chatId, String name) {
        String message = "🏥 <b>Welcome to Vaidyo!</b>\n\n"
                + "Hi <b>" + name + "</b>!\n\n"
                + "Your Telegram is now linked to Vaidyo.\n"
                + "You will receive:\n"
                + "✅ Medicine reminders\n"
                + "✅ Health alerts\n"
                + "✅ Appointment updates\n\n"
                + "Stay healthy! 💪";

        sendMessage(chatId, message);
    }
}