package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_locations")
public class DoctorLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Read-only reference — never modify DoctorProfile itself, only look it up by ID.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_profile_id", nullable = false, unique = true)
    private DoctorProfile doctorProfile;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "location_label", length = 150)
    private String locationLabel;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DoctorLocation() {}

    public Long getId() { return id; }
    public DoctorProfile getDoctorProfile() { return doctorProfile; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setDoctorProfile(DoctorProfile doctorProfile) { this.doctorProfile = doctorProfile; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}