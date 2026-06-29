package com.vaidyo.vaidyo_backend.dto;

public class DoctorRatingRequest {

    private Long appointmentId; // must be a COMPLETED appointment
    private Long patientId;
    private Integer stars;      // 1–5
    private String comment;     // optional

    public DoctorRatingRequest() {}

    public Long getAppointmentId() { return appointmentId; }
    public Long getPatientId() { return patientId; }
    public Integer getStars() { return stars; }
    public String getComment() { return comment; }

    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setStars(Integer stars) { this.stars = stars; }
    public void setComment(String comment) { this.comment = comment; }
}