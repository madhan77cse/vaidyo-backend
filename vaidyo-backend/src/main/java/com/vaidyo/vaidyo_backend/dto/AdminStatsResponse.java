package com.vaidyo.vaidyo_backend.dto;

public class AdminStatsResponse {

    private long totalPatients;
    private long totalDoctors;
    private long totalCaretakers;
    private long pendingDoctorVerifications;
    private long approvedDoctors;
    private long rejectedDoctors;

    public AdminStatsResponse() {}

    public AdminStatsResponse(long totalPatients,
                              long totalDoctors,
                              long totalCaretakers,
                              long pendingDoctorVerifications,
                              long approvedDoctors,
                              long rejectedDoctors) {
        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.totalCaretakers = totalCaretakers;
        this.pendingDoctorVerifications = pendingDoctorVerifications;
        this.approvedDoctors = approvedDoctors;
        this.rejectedDoctors = rejectedDoctors;
    }

    public long getTotalPatients() { return totalPatients; }
    public long getTotalDoctors() { return totalDoctors; }
    public long getTotalCaretakers() { return totalCaretakers; }
    public long getPendingDoctorVerifications() { return pendingDoctorVerifications; }
    public long getApprovedDoctors() { return approvedDoctors; }
    public long getRejectedDoctors() { return rejectedDoctors; }

    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }
    public void setTotalDoctors(long totalDoctors) { this.totalDoctors = totalDoctors; }
    public void setTotalCaretakers(long totalCaretakers) { this.totalCaretakers = totalCaretakers; }
    public void setPendingDoctorVerifications(long pendingDoctorVerifications) { this.pendingDoctorVerifications = pendingDoctorVerifications; }
    public void setApprovedDoctors(long approvedDoctors) { this.approvedDoctors = approvedDoctors; }
    public void setRejectedDoctors(long rejectedDoctors) { this.rejectedDoctors = rejectedDoctors; }
}