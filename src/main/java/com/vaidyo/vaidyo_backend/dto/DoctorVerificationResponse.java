package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.DoctorProfile;

public class DoctorVerificationResponse {

    private Long doctorProfileId;
    private Long userId;
    private String fullName;
    private String mobileNumber;
    private String speciality;
    private String licenseNumber;
    private Integer experienceYears;
    private Double consultationFee;
    private String clinicAddress;
    private String bio;
    private String certificateUrl;
    private String verificationStatus;
    private String rejectionReason;

    public DoctorVerificationResponse() {}

    public static DoctorVerificationResponse from(DoctorProfile profile) {
        DoctorVerificationResponse dto = new DoctorVerificationResponse();
        dto.setDoctorProfileId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setFullName(profile.getUser().getFullName());
        dto.setMobileNumber(profile.getUser().getMobileNumber());
        dto.setSpeciality(profile.getSpeciality().name());
        dto.setLicenseNumber(profile.getLicenseNumber());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setConsultationFee(profile.getConsultationFee());
        dto.setClinicAddress(profile.getClinicAddress());
        dto.setBio(profile.getBio());
        dto.setCertificateUrl(profile.getCertificateUrl());
        dto.setVerificationStatus(profile.getVerificationStatus().name());
        dto.setRejectionReason(profile.getRejectionReason());
        return dto;
    }

    public Long getDoctorProfileId() { return doctorProfileId; }
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getMobileNumber() { return mobileNumber; }
    public String getSpeciality() { return speciality; }
    public String getLicenseNumber() { return licenseNumber; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getConsultationFee() { return consultationFee; }
    public String getClinicAddress() { return clinicAddress; }
    public String getBio() { return bio; }
    public String getCertificateUrl() { return certificateUrl; }
    public String getVerificationStatus() { return verificationStatus; }
    public String getRejectionReason() { return rejectionReason; }

    public void setDoctorProfileId(Long doctorProfileId) { this.doctorProfileId = doctorProfileId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setClinicAddress(String clinicAddress) { this.clinicAddress = clinicAddress; }
    public void setBio(String bio) { this.bio = bio; }
    public void setCertificateUrl(String certificateUrl) { this.certificateUrl = certificateUrl; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}