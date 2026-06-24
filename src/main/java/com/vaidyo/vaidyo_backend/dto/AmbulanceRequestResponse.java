package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.AmbulanceStatus;

import java.time.LocalDateTime;

public class AmbulanceRequestResponse {

    private Long id;
    private String pickupAddress;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String notes;A
    private AmbulanceStatus status;
    private String partnerName;
    private String partnerPhoneNumber;
    private String partnerVehicleNumber;
    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    public AmbulanceRequestResponse() {}

    public AmbulanceRequestResponse(Long id, String pickupAddress, Double pickupLatitude, Double pickupLongitude,
                                    String notes, AmbulanceStatus status, String partnerName,
                                    String partnerPhoneNumber, String partnerVehicleNumber,
                                    LocalDateTime requestedAt, LocalDateTime updatedAt, LocalDateTime completedAt) {
        this.id = id;
        this.pickupAddress = pickupAddress;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.notes = notes;
        this.status = status;
        this.partnerName = partnerName;
        this.partnerPhoneNumber = partnerPhoneNumber;
        this.partnerVehicleNumber = partnerVehicleNumber;
        this.requestedAt = requestedAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    public Long getId() { return id; }
    public String getPickupAddress() { return pickupAddress; }
    public Double getPickupLatitude() { return pickupLatitude; }
    public Double getPickupLongitude() { return pickupLongitude; }
    public String getNotes() { return notes; }
    public AmbulanceStatus getStatus() { return status; }
    public String getPartnerName() { return partnerName; }
    public String getPartnerPhoneNumber() { return partnerPhoneNumber; }
    public String getPartnerVehicleNumber() { return partnerVehicleNumber; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setId(Long id) { this.id = id; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(AmbulanceStatus status) { this.status = status; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public void setPartnerPhoneNumber(String partnerPhoneNumber) { this.partnerPhoneNumber = partnerPhoneNumber; }
    public void setPartnerVehicleNumber(String partnerVehicleNumber) { this.partnerVehicleNumber = partnerVehicleNumber; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}