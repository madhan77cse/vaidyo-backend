package com.vaidyo.vaidyo_backend.dto;

public class AmbulancePartnerResponse {

    private Long id;
    private String partnerName;
    private String phoneNumber;
    private String vehicleNumber;
    private boolean available;
    private Double baseLatitude;
    private Double baseLongitude;

    public AmbulancePartnerResponse() {}

    public AmbulancePartnerResponse(Long id, String partnerName, String phoneNumber, String vehicleNumber,
                                    boolean available, Double baseLatitude, Double baseLongitude) {
        this.id = id;
        this.partnerName = partnerName;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.available = available;
        this.baseLatitude = baseLatitude;
        this.baseLongitude = baseLongitude;
    }

    public Long getId() { return id; }
    public String getPartnerName() { return partnerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public boolean isAvailable() { return available; }
    public Double getBaseLatitude() { return baseLatitude; }
    public Double getBaseLongitude() { return baseLongitude; }

    public void setId(Long id) { this.id = id; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setBaseLatitude(Double baseLatitude) { this.baseLatitude = baseLatitude; }
    public void setBaseLongitude(Double baseLongitude) { this.baseLongitude = baseLongitude; }
}