package com.vaidyo.vaidyo_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nurse_profiles")
public class NurseProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash", "password"})
    private User user;

    @Column(name = "nursing_council_reg_number", nullable = false, unique = true, length = 50)
    private String nursingCouncilRegNumber;

    @Column(name = "id_document_url", nullable = false, length = 255)
    private String idDocumentUrl;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "service_address", columnDefinition = "TEXT")
    private String serviceAddress;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", length = 20)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public NurseProfile() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getNursingCouncilRegNumber() { return nursingCouncilRegNumber; }
    public String getIdDocumentUrl() { return idDocumentUrl; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getConsultationFee() { return consultationFee; }
    public String getServiceAddress() { return serviceAddress; }
    public String getBio() { return bio; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public String getRejectionReason() { return rejectionReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setNursingCouncilRegNumber(String nursingCouncilRegNumber) { this.nursingCouncilRegNumber = nursingCouncilRegNumber; }
    public void setIdDocumentUrl(String idDocumentUrl) { this.idDocumentUrl = idDocumentUrl; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }
    public void setBio(String bio) { this.bio = bio; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED
    }
}