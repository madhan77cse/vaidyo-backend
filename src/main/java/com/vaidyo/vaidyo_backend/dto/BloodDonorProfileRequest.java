package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BloodDonorProfileRequest {

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private String locationLabel;

    private LocalDate lastDonationDate;

    private String contactPreference; // "PHONE" or "TELEGRAM"

    private String phoneNumber;

    private Boolean available;

    public BloodDonorProfileRequest() {}

    public String getBloodGroup() { return bloodGroup; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public String getContactPreference() { return contactPreference; }
    public String getPhoneNumber() { return phoneNumber; }
    public Boolean getAvailable() { return available; }

    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }
    public void setContactPreference(String contactPreference) { this.contactPreference = contactPreference; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAvailable(Boolean available) { this.available = available; }
}