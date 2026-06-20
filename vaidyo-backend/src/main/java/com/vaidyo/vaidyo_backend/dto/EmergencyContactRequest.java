package com.vaidyo.vaidyo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmergencyContactRequest {

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String relationship;

    private Integer priorityOrder;

    public String getContactName() { return contactName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRelationship() { return relationship; }
    public Integer getPriorityOrder() { return priorityOrder; }

    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public void setPriorityOrder(Integer priorityOrder) { this.priorityOrder = priorityOrder; }
}