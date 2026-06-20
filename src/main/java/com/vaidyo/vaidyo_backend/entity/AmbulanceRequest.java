package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ambulance_requests")
public class AmbulanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "pickup_address", columnDefinition = "TEXT", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AmbulanceStatus status = AmbulanceStatus.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_partner_id")
    private AmbulancePartner assignedPartner;

    @CreationTimestamp
    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public AmbulanceRequest() {}

    public Long getId() { return id; }
    public User getPatient() { return patient; }
    public String getPickupAddress() { return pickupAddress; }
    public Double getPickupLatitude() { return pickupLatitude; }
    public Double getPickupLongitude() { return pickupLongitude; }
    public String getNotes() { return notes; }
    public AmbulanceStatus getStatus() { return status; }
    public AmbulancePartner getAssignedPartner() { return assignedPartner; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatient(User patient) { this.patient = patient; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(AmbulanceStatus status) { this.status = status; }
    public void setAssignedPartner(AmbulancePartner assignedPartner) { this.assignedPartner = assignedPartner; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}