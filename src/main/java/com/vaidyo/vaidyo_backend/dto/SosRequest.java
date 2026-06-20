package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotNull;

public class SosRequest {

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    public SosRequest() {}

    public SosRequest(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}