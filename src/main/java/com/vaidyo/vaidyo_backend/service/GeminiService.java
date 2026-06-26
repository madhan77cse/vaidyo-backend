package com.vaidyo.vaidyo_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    // ── Text prompt ────────────────────────────────────────────
    public String generateContent(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    GROQ_URL, entity, Map.class);

            Map body = response.getBody();
            List choices = (List) body.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            System.out.println("GROQ ERROR: " + e.getMessage());
            return "Sorry, I was unable to process your request at this time. Please try again later.";
        }
    }

    // ── Image + text prompt (for lab report images) ────────────
    public String generateContentWithImage(String base64Image,
                                           String mediaType,
                                           String prompt) {
        try {
            Map<String, Object> textContent = Map.of(
                    "type", "text",
                    "text", prompt
            );

            Map<String, Object> imageContent = Map.of(
                    "type", "image_url",
                    "image_url", Map.of(
                            "url", "data:" + mediaType + ";base64," + base64Image
                    )
            );

            Map<String, Object> requestBody = Map.of(
                    "model", "meta-llama/llama-4-scout-17b-16e-instruct",
                    "messages", List.of(
                            Map.of("role", "user",
                                    "content", List.of(textContent, imageContent))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    GROQ_URL, entity, Map.class);

            Map body = response.getBody();
            List choices = (List) body.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            System.out.println("GROQ IMAGE ERROR: " + e.getMessage());
            return "Sorry, I was unable to analyze the image. " +
                    "Please try uploading a clearer image or a PDF version.";
        }
    }
}