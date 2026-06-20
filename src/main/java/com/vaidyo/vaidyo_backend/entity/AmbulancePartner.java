package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ambulance_partners")
public class AmbulancePartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partner_name", nullable = false, length = 100)
    private String partnerName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    // Set once the partner links Telegram (manually, since they're seeded contacts, not app users)
    @Column(name = "telegram_chat_id", length = 50)
    private String telegramChatId;

    @Column(name = "base_latitude")
    private Double baseLatitude;

    @Column(name = "base_longitude")
    private Double baseLongitude;

    @Column(name = "available", nullable = false)
    private boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AmbulancePartner() {}

    public Long getId() { return id; }
    public String getPartnerName() { return partnerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getTelegramChatId() { return telegramChatId; }
    public Double getBaseLatitude() { return baseLatitude; }
    public Double getBaseLongitude() { return baseLongitude; }
    public boolean isAvailable() { return available; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    public void setBaseLatitude(Double baseLatitude) { this.baseLatitude = baseLatitude; }
    public void setBaseLongitude(Double baseLongitude) { this.baseLongitude = baseLongitude; }
    public void setAvailable(boolean available) { this.available = available; }
}