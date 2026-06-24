package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;

public class VideoVisitResponse {

    private Long appointmentId;
    private String roomUrl;
    private LocalDateTime createdAt;

    public VideoVisitResponse() {}

    public VideoVisitResponse(Long appointmentId, String roomUrl, LocalDateTime createdAt) {
        this.appointmentId = appointmentId;
        this.roomUrl = roomUrl;
        this.createdAt = createdAt;
    }

    public Long getAppointmentId() { return appointmentId; }
    public String getRoomUrl() { return roomUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public void setRoomUrl(String roomUrl) { this.roomUrl = roomUrl; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}