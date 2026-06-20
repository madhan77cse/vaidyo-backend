package com.vaidyo.vaidyo_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SosResponse {

    private String patientName;
    private String locationLink;
    private int totalContacts;
    private List<String> contactsNotified;
    private List<UnlinkedContact> contactsToCallManually;
    private LocalDateTime triggeredAt;

    public SosResponse() {}

    public SosResponse(String patientName, String locationLink, int totalContacts,
                       List<String> contactsNotified, List<UnlinkedContact> contactsToCallManually,
                       LocalDateTime triggeredAt) {
        this.patientName = patientName;
        this.locationLink = locationLink;
        this.totalContacts = totalContacts;
        this.contactsNotified = contactsNotified;
        this.contactsToCallManually = contactsToCallManually;
        this.triggeredAt = triggeredAt;
    }

    public String getPatientName() { return patientName; }
    public String getLocationLink() { return locationLink; }
    public int getTotalContacts() { return totalContacts; }
    public List<String> getContactsNotified() { return contactsNotified; }
    public List<UnlinkedContact> getContactsToCallManually() { return contactsToCallManually; }
    public LocalDateTime getTriggeredAt() { return triggeredAt; }

    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setLocationLink(String locationLink) { this.locationLink = locationLink; }
    public void setTotalContacts(int totalContacts) { this.totalContacts = totalContacts; }
    public void setContactsNotified(List<String> contactsNotified) { this.contactsNotified = contactsNotified; }
    public void setContactsToCallManually(List<UnlinkedContact> contactsToCallManually) { this.contactsToCallManually = contactsToCallManually; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }

    // Small nested DTO for contacts that need a manual phone call fallback
    public static class UnlinkedContact {
        private String contactName;
        private String phoneNumber;

        public UnlinkedContact() {}

        public UnlinkedContact(String contactName, String phoneNumber) {
            this.contactName = contactName;
            this.phoneNumber = phoneNumber;
        }

        public String getContactName() { return contactName; }
        public String getPhoneNumber() { return phoneNumber; }

        public void setContactName(String contactName) { this.contactName = contactName; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }
}