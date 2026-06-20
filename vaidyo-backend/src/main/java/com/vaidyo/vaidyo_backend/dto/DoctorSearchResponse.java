package com.vaidyo.vaidyo_backend.dto;

public class DoctorSearchResponse {

    private Long userId;
    private String fullName;
    private String speciality;
    private String licenseNumber;
    private Integer experienceYears;
    private Double consultationFee;
    private String clinicAddress;
    private String bio;
    private String verificationStatus;

    // ── Getters ────────────────────────────────────────────────
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getSpeciality() { return speciality; }
    public String getLicenseNumber() { return licenseNumber; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getConsultationFee() { return consultationFee; }
    public String getClinicAddress() { return clinicAddress; }
    public String getBio() { return bio; }
    public String getVerificationStatus() { return verificationStatus; }

    // ── Setters ────────────────────────────────────────────────
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setClinicAddress(String clinicAddress) { this.clinicAddress = clinicAddress; }
    public void setBio(String bio) { this.bio = bio; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
}