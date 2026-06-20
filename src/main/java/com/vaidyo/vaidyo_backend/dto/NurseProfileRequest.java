package com.vaidyo.vaidyo_backend.dto;

public class NurseProfileRequest {

    private String nursingCouncilRegNumber;
    private String idDocumentUrl;
    private Integer experienceYears;
    private Double consultationFee;
    private String serviceAddress;
    private String bio;

    public NurseProfileRequest() {}

    public String getNursingCouncilRegNumber() { return nursingCouncilRegNumber; }
    public String getIdDocumentUrl() { return idDocumentUrl; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getConsultationFee() { return consultationFee; }
    public String getServiceAddress() { return serviceAddress; }
    public String getBio() { return bio; }

    public void setNursingCouncilRegNumber(String nursingCouncilRegNumber) { this.nursingCouncilRegNumber = nursingCouncilRegNumber; }
    public void setIdDocumentUrl(String idDocumentUrl) { this.idDocumentUrl = idDocumentUrl; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }
    public void setBio(String bio) { this.bio = bio; }
}