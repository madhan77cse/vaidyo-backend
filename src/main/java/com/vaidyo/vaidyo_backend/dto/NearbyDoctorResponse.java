package com.vaidyo.vaidyo_backend.dto;

public class NearbyDoctorResponse {

    private Long doctorProfileId;
    private String doctorName;
    private String speciality;
    private Double consultationFee;
    private String clinicAddress;
    private Double latitude;
    private Double longitude;
    private String locationLabel;
    private Double distanceKm;

    public NearbyDoctorResponse() {}

    public NearbyDoctorResponse(Long doctorProfileId, String doctorName, String speciality,
                                Double consultationFee, String clinicAddress, Double latitude,
                                Double longitude, String locationLabel, Double distanceKm) {
        this.doctorProfileId = doctorProfileId;
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.consultationFee = consultationFee;
        this.clinicAddress = clinicAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationLabel = locationLabel;
        this.distanceKm = distanceKm;
    }

    public Long getDoctorProfileId() { return doctorProfileId; }
    public String getDoctorName() { return doctorName; }
    public String getSpeciality() { return speciality; }
    public Double getConsultationFee() { return consultationFee; }
    public String getClinicAddress() { return clinicAddress; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getLocationLabel() { return locationLabel; }
    public Double getDistanceKm() { return distanceKm; }

    public void setDoctorProfileId(Long doctorProfileId) { this.doctorProfileId = doctorProfileId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public void setClinicAddress(String clinicAddress) { this.clinicAddress = clinicAddress; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
}