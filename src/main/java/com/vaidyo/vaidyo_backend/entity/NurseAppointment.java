package com.vaidyo.vaidyo_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nurse_appointments")
public class NurseAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nurse_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User nurse;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "patient_address", columnDefinition = "TEXT")
    private String patientAddress;

    @Column(name = "symptoms", columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "nurse_notes", columnDefinition = "TEXT")
    private String nurseNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NurseAppointmentStatus status = NurseAppointmentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public NurseAppointment() {}

    public Long getId() { return id; }
    public User getPatient() { return patient; }
    public User getNurse() { return nurse; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getPatientAddress() { return patientAddress; }
    public String getSymptoms() { return symptoms; }
    public Double getFee() { return fee; }
    public String getNurseNotes() { return nurseNotes; }
    public NurseAppointmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatient(User patient) { this.patient = patient; }
    public void setNurse(User nurse) { this.nurse = nurse; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setPatientAddress(String patientAddress) { this.patientAddress = patientAddress; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setFee(Double fee) { this.fee = fee; }
    public void setNurseNotes(String nurseNotes) { this.nurseNotes = nurseNotes; }
    public void setStatus(NurseAppointmentStatus status) { this.status = status; }

    public enum NurseAppointmentStatus {
        PENDING, REPLIED, CONFIRMED, REJECTED, COMPLETED, CANCELLED
    }
}