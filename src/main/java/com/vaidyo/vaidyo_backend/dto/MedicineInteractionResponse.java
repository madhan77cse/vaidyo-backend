package com.vaidyo.vaidyo_backend.dto;

public class MedicineInteractionResponse {

    private String newMedicine;
    private String existingMedicine;
    private String description;
    private String severity;

    public MedicineInteractionResponse(String newMedicine,
                                       String existingMedicine, String description, String severity) {
        this.newMedicine = newMedicine;
        this.existingMedicine = existingMedicine;
        this.description = description;
        this.severity = severity;
    }

    public String getNewMedicine() { return newMedicine; }
    public String getExistingMedicine() { return existingMedicine; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }

    public void setNewMedicine(String newMedicine) { this.newMedicine = newMedicine; }
    public void setExistingMedicine(String existingMedicine) { this.existingMedicine = existingMedicine; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(String severity) { this.severity = severity; }
}