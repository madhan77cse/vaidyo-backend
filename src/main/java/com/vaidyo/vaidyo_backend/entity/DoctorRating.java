package com.vaidyo.vaidyo_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "doctor_ratings",
        uniqueConstraints = {
                // One rating per patient per appointment — enforced at DB level
                @UniqueConstraint(columnNames = {"appointment_id"})
        }
)
public class DoctorRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_profile_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DoctorProfile doctorProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User patient;

    // Link to appointment — ensures one rating per completed appointment
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Appointment appointment;

    @Column(name = "stars", nullable = false)
    private Integer stars; // 1 to 5

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DoctorRating() {}

    public Long getId() { return id; }
    public DoctorProfile getDoctorProfile() { return doctorProfile; }
    public User getPatient() { return patient; }
    public Appointment getAppointment() { return appointment; }
    public Integer getStars() { return stars; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setDoctorProfile(DoctorProfile doctorProfile) { this.doctorProfile = doctorProfile; }
    public void setPatient(User patient) { this.patient = patient; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public void setStars(Integer stars) { this.stars = stars; }
    public void setComment(String comment) { this.comment = comment; }
}
