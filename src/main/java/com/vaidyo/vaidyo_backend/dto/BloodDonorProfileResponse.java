package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BloodDonorProfileResponse {

    private Long id;
    private String donorName;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;
    private String locationLabel;
    private LocalDate lastDonationDate;
    private String contactPreference;
    private String phoneNumber;
    private boolean available;
    private Double distanceKm; // only populated during search results, null otherwise
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BloodDonorProfileResponse() {}

    public BloodDonorProfileResponse(Long id, String donorName, String bloodGroup, Double latitude,
                                     Double longitude, String locationLabel, LocalDate lastDonationDate,
                                     String contactPreference, String phoneNumber, boolean available,
                                     Double distanceKm, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.donorName = donorName;
        this.bloodGroup = bloodGroup;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationLabel = locationLabel;
        this.lastDonationDate = lastDonationDate;
        this.contactPreference = contactPreference;
        this.phoneNumber = phoneNumber;
        this.available = available;
        this.distanceKm = distanceKm;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getDonorName() { return donorName; }
    public String getBloodGroup() { return bloodGroup; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public String getContactPreference() { return contactPreference; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isAvailable() { return available; }
    public Double getDistanceKm() { return distanceKm; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }
    public void setContactPreference(String contactPreference) { this.contactPreference = contactPreference; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}