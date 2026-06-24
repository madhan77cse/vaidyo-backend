package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_visits")
public class VideoVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Read-only reference to the Appointment owned by another teammate.
    // We never modify Appointment itself, only look it up by ID.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(name = "room_url", nullable = false, length = 255)
    private String roomUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public VideoVisit() {}

    public Long getId() { return id; }
    public Appointment getAppointment() { return appointment; }
    public String getRoomUrl() { return roomUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public void setRoomUrl(String roomUrl) { this.roomUrl = roomUrl; }
}