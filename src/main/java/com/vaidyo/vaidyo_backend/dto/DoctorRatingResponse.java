package com.vaidyo.vaidyo_backend.dto;

import com.vaidyo.vaidyo_backend.entity.DoctorRating;

import java.time.LocalDateTime;

public class DoctorRatingResponse {

    private Long id;
    private Long doctorProfileId;
    private String patientName;
    private Long appointmentId;
    private Integer stars;
    private String comment;
    private LocalDateTime createdAt;

    public DoctorRatingResponse() {}

    public DoctorRatingResponse(DoctorRating rating) {
        this.id = rating.getId();
        this.doctorProfileId = rating.getDoctorProfile().getId();
        this.patientName = rating.getPatient().getFullName();
        this.appointmentId = rating.getAppointment().getId();
        this.stars = rating.getStars();
        this.comment = rating.getComment();
        this.createdAt = rating.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getDoctorProfileId() { return doctorProfileId; }
    public String getPatientName() { return patientName; }
    public Long getAppointmentId() { return appointmentId; }
    public Integer getStars() { return stars; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setDoctorProfileId(Long doctorProfileId) { this.doctorProfileId = doctorProfileId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public void setStars(Integer stars) { this.stars = stars; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}