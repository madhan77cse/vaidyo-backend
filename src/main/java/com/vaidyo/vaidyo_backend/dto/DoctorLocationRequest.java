package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotNull;

public class DoctorLocationRequest {

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private String locationLabel;

    public DoctorLocationRequest() {}

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
}