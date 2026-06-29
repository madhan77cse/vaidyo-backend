package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;

public class BloodRequestResponse {

    private Long id;
    private String requesterName;
    private String bloodGroupNeeded;
    private String urgency;
    private Double latitude;
    private Double longitude;
    private String locationLabel;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BloodRequestResponse() {}

    public BloodRequestResponse(Long id, String requesterName, String bloodGroupNeeded, String urgency,
                                Double latitude, Double longitude, String locationLabel, String notes,
                                String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requesterName = requesterName;
        this.bloodGroupNeeded = bloodGroupNeeded;
        this.urgency = urgency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationLabel = locationLabel;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getRequesterName() { return requesterName; }
    public String getBloodGroupNeeded() { return bloodGroupNeeded; }
    public String getUrgency() { return urgency; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }
    public void setBloodGroupNeeded(String bloodGroupNeeded) { this.bloodGroupNeeded = bloodGroupNeeded; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}