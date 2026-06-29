package com.vaidyo.vaidyo_backend.dto;

public class NurseAppointmentRequest {

    private Long nurseId;
    private String patientAddress;
    private String symptoms;

    public NurseAppointmentRequest() {}

    public Long getNurseId() { return nurseId; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }

    public void setNurseId(Long nurseId) { this.nurseId = nurseId; }
    public void setPatientAddress(String patientAddress) { this.patientAddress = patientAddress; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
}