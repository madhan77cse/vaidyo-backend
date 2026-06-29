
package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BloodRequestCreate {

    @NotBlank(message = "Blood group needed is required")
    private String bloodGroupNeeded;

    @NotBlank(message = "Urgency is required")
    private String urgency; // "LOW", "MEDIUM", "HIGH", "CRITICAL"

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private String locationLabel;

    private String notes;

    public BloodRequestCreate() {}

    public String getBloodGroupNeeded() { return bloodGroupNeeded; }
    public String getUrgency() { return urgency; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public String getNotes() { return notes; }

    public void setBloodGroupNeeded(String bloodGroupNeeded) { this.bloodGroupNeeded = bloodGroupNeeded; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setNotes(String notes) { this.notes = notes; }
}