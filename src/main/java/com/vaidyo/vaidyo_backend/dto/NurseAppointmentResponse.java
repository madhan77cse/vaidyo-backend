package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;

public class NurseAppointmentResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long nurseId;
    private String nurseName;
    private LocalDateTime scheduledAt;
    private String patientAddress;
    private String symptoms;
    private Double fee;
    private String nurseNotes;
    private String status;
    private LocalDateTime createdAt;

    public NurseAppointmentResponse() {}

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Long getNurseId() { return nurseId; }
    public String getNurseName() { return nurseName; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }
    public Double getFee() { return fee; }
    public String getNurseNotes() { return nurseNotes; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setNurseId(Long nurseId) { this.nurseId = nurseId; }
    public void setNurseName(String nurseName) { this.nurseName = nurseName; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setPatientAddress(String patientAddress) { this.patientAddress = patientAddress; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setFee(Double fee) { this.fee = fee; }
    public void setNurseNotes(String nurseNotes) { this.nurseNotes = nurseNotes; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}