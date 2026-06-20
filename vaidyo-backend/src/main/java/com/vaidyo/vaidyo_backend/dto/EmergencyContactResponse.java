package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;

public class EmergencyContactResponse {

    private Long id;
    private String contactName;
    private String phoneNumber;
    private String relationship;
    private Integer priorityOrder;
    private boolean telegramLinked;
    private LocalDateTime createdAt;

    public EmergencyContactResponse() {}

    public EmergencyContactResponse(Long id, String contactName, String phoneNumber,
                                    String relationship, Integer priorityOrder,
                                    boolean telegramLinked, LocalDateTime createdAt) {
        this.id = id;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.priorityOrder = priorityOrder;
        this.telegramLinked = telegramLinked;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getContactName() { return contactName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRelationship() { return relationship; }
    public Integer getPriorityOrder() { return priorityOrder; }
    public boolean isTelegramLinked() { return telegramLinked; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public void setPriorityOrder(Integer priorityOrder) { this.priorityOrder = priorityOrder; }
    public void setTelegramLinked(boolean telegramLinked) { this.telegramLinked = telegramLinked; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}