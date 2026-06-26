package com.vaidyo.vaidyo_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TelegramPollingService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramLinkService telegramLinkService;
    private final TelegramService telegramService;
    private final TelegramDoseService telegramDoseService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private long lastUpdateId = 0;

    private static final String TELEGRAM_API =
            "https://api.telegram.org/bot";

    public TelegramPollingService(
            TelegramLinkService telegramLinkService,
            TelegramService telegramService,
            TelegramDoseService telegramDoseService) {
        this.telegramLinkService = telegramLinkService;
        this.telegramService = telegramService;
        this.telegramDoseService = telegramDoseService;
    }

    @Scheduled(fixedRate = 3000)
    public void pollUpdates() {
        try {
            String url = TELEGRAM_API + botToken
                    + "/getUpdates?offset="
                    + (lastUpdateId + 1)
                    + "&timeout=1";

            String response =
                    restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("result");

            if (results == null || !results.isArray()) return;

            for (JsonNode update : results) {
                long updateId = update.get("update_id").asLong();
                lastUpdateId = updateId;

                JsonNode message = update.get("message");
                if (message == null) continue;

                String chatId = message.get("chat")
                        .get("id").asText();
                String text = message.has("text")
                        ? message.get("text").asText() : "";

                handleMessage(chatId, text);
            }

        } catch (Exception e) {
            System.err.println("Polling error: " + e.getMessage());
        }
    }

    private void handleMessage(String chatId, String text) {

        // Handle /start command
        if (text.equals("/start")) {
            telegramService.sendMessage(chatId,
                    "👋 Welcome to <b>Vaidyo Health Bot!</b>\n\n"
                            + "Please send your <b>6-digit link code</b> "
                            + "from the Vaidyo app to connect your account.");
            return;
        }

        // Handle 6-digit code
        if (text.matches("\\d{6}")) {
            String result =
                    telegramLinkService.verifyAndLink(text, chatId);

            if (result.equals("SUCCESS")) {
                telegramService.sendMessage(chatId,
                        "✅ <b>Account linked successfully!</b>\n\n"
                                + "You will now receive:\n"
                                + "💊 Medicine reminders\n"
                                + "🏥 Health alerts\n"
                                + "📅 Appointment updates");
            } else {
                telegramService.sendMessage(chatId,
                        "❌ Invalid code. Please check the code "
                                + "in your Vaidyo app and try again.");
            }
            return;
        }

        // ── Handle "yes" reply → mark dose taken ──────────────
        if (text.equalsIgnoreCase("yes")
                || text.equalsIgnoreCase("taken")
                || text.equalsIgnoreCase("done")) {
            boolean marked = telegramDoseService.markLatestDoseTaken(chatId);
            if (marked) {
                telegramService.sendMessage(chatId,
                        "✅ <b>Dose marked as taken!</b>\n\n"
                                + "Great job staying on track with your medication. 💪");
            } else {
                telegramService.sendMessage(chatId,
                        "⚠️ No pending dose found to mark as taken.\n"
                                + "If you think this is an error, "
                                + "please check the Vaidyo app.");
            }
            return;
        }

        // Handle anything else
        telegramService.sendMessage(chatId,
                "Please send your <b>6-digit link code</b> "
                        + "from the Vaidyo app.\n\n"
                        + "Or reply <b>yes</b> after taking your medicine "
                        + "to mark your dose as taken. ✅");
    }
}