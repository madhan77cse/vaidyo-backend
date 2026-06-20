package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AmbulanceRequestCreate {

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotNull(message = "Pickup latitude is required")
    private Double pickupLatitude;

    @NotNull(message = "Pickup longitude is required")
    private Double pickupLongitude;

    private String notes;

    public AmbulanceRequestCreate() {}

    public String getPickupAddress() { return pickupAddress; }
    public Double getPickupLatitude() { return pickupLatitude; }
    public Double getPickupLongitude() { return pickupLongitude; }
    public String getNotes() { return notes; }

    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
    public void setNotes(String notes) { this.notes = notes; }
}