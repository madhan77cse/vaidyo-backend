package com.vaidyo.vaidyo_backend.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class MessageService {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
            "en", "ta", "hi", "te", "kn", "ml", "mr", "bn", "gu", "pa", "or", "ur"
    );

    private static final String DEFAULT_LANGUAGE = "en";

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get a message by key and language code, with optional template variables.
     * Falls back to English if the language is unsupported or the key is missing.
     */
    public String get(String key, String language, Object... args) {
        String lang = (language != null && SUPPORTED_LANGUAGES.contains(language.toLowerCase()))
                ? language.toLowerCase()
                : DEFAULT_LANGUAGE;

        try {
            Locale locale = Locale.forLanguageTag(lang);
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            // Fall back to English if key missing in target language
            try {
                return messageSource.getMessage(key, args, Locale.ENGLISH);
            } catch (Exception fallbackException) {
                return key; // Last resort: return the raw key
            }
        }
    }

    public boolean isSupported(String language) {
        return language != null && SUPPORTED_LANGUAGES.contains(language.toLowerCase());
    }
}