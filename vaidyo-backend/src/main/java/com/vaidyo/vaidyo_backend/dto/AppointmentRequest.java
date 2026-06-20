package com.vaidyo.vaidyo_backend.dto;

public class AppointmentRequest {

    private Long doctorId;
    private String patientAddress;
    private String symptoms;
    private String preferredDate;
    private String preferredTime;

    // ── Getters ────────────────────────────────────────────────
    public Long getDoctorId() { return doctorId; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }
    public String getPreferredDate() { return preferredDate; }
    public String getPreferredTime() { return preferredTime; }

    // ── Setters ────────────────────────────────────────────────
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setPatientAddress(String patientAddress) { this.patientAddress = patientAddress; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }
}