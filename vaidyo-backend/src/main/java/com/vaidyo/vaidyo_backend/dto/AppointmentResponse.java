package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String speciality;
    private LocalDateTime scheduledAt;
    private String patientAddress;
    private String symptoms;
    private Double fee;
    private String doctorNotes;
    private String status;
    private LocalDateTime createdAt;

    // ── Getters ────────────────────────────────────────────────
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpeciality() { return speciality; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }
    public Double getFee() { return fee; }
    public String getDoctorNotes() { return doctorNotes; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ────────────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setPatientAddress(String patientAddress) { this.patientAddress = patientAddress; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setFee(Double fee) { this.fee = fee; }
    public void setDoctorNotes(String doctorNotes) { this.doctorNotes = doctorNotes; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}