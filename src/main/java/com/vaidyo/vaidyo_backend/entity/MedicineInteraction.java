package com.vaidyo.vaidyo_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicine_interactions")
public class MedicineInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_a", nullable = false, length = 100)
    private String medicineA;

    @Column(name = "medicine_b", nullable = false, length = 100)
    private String medicineB;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "severity", length = 20)
    private String severity;

    public MedicineInteraction() {}

    public Long getId() { return id; }
    public String getMedicineA() { return medicineA; }
    public String getMedicineB() { return medicineB; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }

    public void setId(Long id) { this.id = id; }
    public void setMedicineA(String medicineA) { this.medicineA = medicineA; }
    public void setMedicineB(String medicineB) { this.medicineB = medicineB; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(String severity) { this.severity = severity; }
}