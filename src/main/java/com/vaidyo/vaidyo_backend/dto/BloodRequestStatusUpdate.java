package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class BloodRequestStatusUpdate {

    @NotBlank(message = "Status is required")
    private String status; // "FULFILLED" or "CANCELLED"

    public BloodRequestStatusUpdate() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}