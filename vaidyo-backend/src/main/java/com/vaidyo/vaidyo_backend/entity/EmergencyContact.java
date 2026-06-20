package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "relationship", length = 50)
    private String relationship;

    // Populated once the contact links their Telegram via the bot (next step)
    @Column(name = "telegram_chat_id", length = 50)
    private String telegramChatId;

    @Column(name = "priority_order")
    private Integer priorityOrder = 1;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public EmergencyContact() {}

    public EmergencyContact(User patient, String contactName, String phoneNumber,
                            String relationship, Integer priorityOrder) {
        this.patient = patient;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.priorityOrder = priorityOrder;
    }

    public Long getId() { return id; }
    public User getPatient() { return patient; }
    public String getContactName() { return contactName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRelationship() { return relationship; }
    public String getTelegramChatId() { return telegramChatId; }
    public Integer getPriorityOrder() { return priorityOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatient(User patient) { this.patient = patient; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    public void setPriorityOrder(Integer priorityOrder) { this.priorityOrder = priorityOrder; }
}