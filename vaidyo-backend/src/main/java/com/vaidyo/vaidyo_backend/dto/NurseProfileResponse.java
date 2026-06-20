package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.NurseProfile;

public class NurseProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String mobileNumber;
    private String nursingCouncilRegNumber;
    private String idDocumentUrl;
    private Integer experienceYears;
    private Double consultationFee;
    private String serviceAddress;
    private String bio;
    private String verificationStatus;
    private String rejectionReason;

    public NurseProfileResponse() {}

    public NurseProfileResponse(NurseProfile profile) {
        this.id = profile.getId();
        this.userId = profile.getUser().getId();
        this.fullName = profile.getUser().getFullName();
        this.mobileNumber = profile.getUser().getMobileNumber();
        this.nursingCouncilRegNumber = profile.getNursingCouncilRegNumber();
        this.idDocumentUrl = profile.getIdDocumentUrl();
        this.experienceYears = profile.getExperienceYears();
        this.consultationFee = profile.getConsultationFee();
        this.serviceAddress = profile.getServiceAddress();
        this.bio = profile.getBio();
        this.verificationStatus = profile.getVerificationStatus().name();
        this.rejectionReason = profile.getRejectionReason();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getMobileNumber() { return mobileNumber; }
    public String getNursingCouncilRegNumber() { return nursingCouncilRegNumber; }
    public String getIdDocumentUrl() { return idDocumentUrl; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getConsultationFee() { return consultationFee; }
    public String getServiceAddress() { return serviceAddress; }
    public String getBio() { return bio; }
    public String getVerificationStatus() { return verificationStatus; }
    public String getRejectionReason() { return rejectionReason; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setNursingCouncilRegNumber(String nursingCouncilRegNumber) { this.nursingCouncilRegNumber = nursingCouncilRegNumber; }
    public void setIdDocumentUrl(String idDocumentUrl) { this.idDocumentUrl = idDocumentUrl; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }
    public void setBio(String bio) { this.bio = bio; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}