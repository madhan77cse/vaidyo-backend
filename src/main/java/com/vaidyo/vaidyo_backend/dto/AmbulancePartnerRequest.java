package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AmbulancePartnerRequest {

    @NotBlank(message = "Partner name is required")
    private String partnerName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String vehicleNumber;
    private String telegramChatId;
    private Double baseLatitude;
    private Double baseLongitude;
    private Boolean available;

    public AmbulancePartnerRequest() {}

    public String getPartnerName() { return partnerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getTelegramChatId() { return telegramChatId; }
    public Double getBaseLatitude() { return baseLatitude; }
    public Double getBaseLongitude() { return baseLongitude; }
    public Boolean getAvailable() { return available; }

    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    public void setBaseLatitude(Double baseLatitude) { this.baseLatitude = baseLatitude; }
    public void setBaseLongitude(Double baseLongitude) { this.baseLongitude = baseLongitude; }
    public void setAvailable(Boolean available) { this.available = available; }
}