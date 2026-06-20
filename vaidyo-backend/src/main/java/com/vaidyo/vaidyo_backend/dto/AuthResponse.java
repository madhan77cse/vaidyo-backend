package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.User;

public class AuthResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String fullName;
    private String mobileNumber;
    private User.Role role;
    private String message;
    private Long patientId;
    private String preferredLanguage;

    // ── Getters ────────────────────────────────────────────────
    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getMobileNumber() { return mobileNumber; }
    public User.Role getRole() { return role; }
    public String getMessage() { return message; }
    public Long getPatientId() { return patientId; }
    public String getPreferredLanguage() { return preferredLanguage; }

    // ── Setters ────────────────────────────────────────────────
    public void setToken(String token) { this.token = token; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setRole(User.Role role) { this.role = role; }
    public void setMessage(String message) { this.message = message; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}