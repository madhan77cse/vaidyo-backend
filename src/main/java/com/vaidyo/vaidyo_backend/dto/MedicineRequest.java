package com.vaidyo.vaidyo_backend.dto;

<<<<<<< HEAD
=======
import java.time.LocalDate;

>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
public class MedicineRequest {

    private String medicineName;
    private String dosage;
    private String frequency;
    private String reminderTime;
    private String notes;
<<<<<<< HEAD

    // ── Getters ────────────────────────────────────────────────
=======
    private LocalDate expiryDate;

>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
    public String getMedicineName() { return medicineName; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public String getReminderTime() { return reminderTime; }
    public String getNotes() { return notes; }
<<<<<<< HEAD

    // ── Setters ────────────────────────────────────────────────
=======
    public LocalDate getExpiryDate() { return expiryDate; }

>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public void setNotes(String notes) { this.notes = notes; }
<<<<<<< HEAD
=======
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
}