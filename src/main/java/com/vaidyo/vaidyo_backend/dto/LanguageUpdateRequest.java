package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LanguageUpdateRequest {

    @NotBlank(message = "Language code is required")
    private String language;

    public LanguageUpdateRequest() {}

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}