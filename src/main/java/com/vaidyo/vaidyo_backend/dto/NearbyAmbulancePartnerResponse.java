package com.vaidyo.vaidyo_backend.dto;

public class NearbyAmbulancePartnerResponse {

    private Long id;
    private String partnerName;
    private String phoneNumber;
    private String vehicleNumber;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;

    public NearbyAmbulancePartnerResponse() {}

    public NearbyAmbulancePartnerResponse(Long id, String partnerName, String phoneNumber, String vehicleNumber,
                                          Double latitude, Double longitude, Double distanceKm) {
        this.id = id;
        this.partnerName = partnerName;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
    }

    public Long getId() { return id; }
    public String getPartnerName() { return partnerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getDistanceKm() { return distanceKm; }

    public void setId(Long id) { this.id = id; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
}