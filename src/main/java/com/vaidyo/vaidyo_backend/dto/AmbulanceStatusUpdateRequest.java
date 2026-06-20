package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.AmbulanceStatus;
import jakarta.validation.constraints.NotNull;

public class AmbulanceStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private AmbulanceStatus status;

    public AmbulanceStatusUpdateRequest() {}

    public AmbulanceStatus getStatus() { return status; }
    public void setStatus(AmbulanceStatus status) { this.status = status; }
}