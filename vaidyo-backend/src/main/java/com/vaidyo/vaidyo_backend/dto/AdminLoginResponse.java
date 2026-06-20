package com.vaidyo.vaidyo_backend.dto;

public class AdminLoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long adminId;
    private String username;
    private String fullName;
    private String email;
    private String message;

    public AdminLoginResponse() {}

    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public Long getAdminId() { return adminId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }

    public void setToken(String token) { this.token = token; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public void setUsername(String username) { this.username = username; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setMessage(String message) { this.message = message; }
}