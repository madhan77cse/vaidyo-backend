package com.vaidyo.vaidyo_backend.config;

import com.vaidyo.vaidyo_backend.entity.MedicineInteraction;
import com.vaidyo.vaidyo_backend.repository.MedicineInteractionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MedicineInteractionSeeder implements CommandLineRunner {

    private final MedicineInteractionRepository repository;

    public MedicineInteractionSeeder(MedicineInteractionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return; // already seeded

        String[][] interactions = {
                {"Warfarin", "Aspirin", "Increased bleeding risk", "HIGH"},
                {"Warfarin", "Ibuprofen", "Increased bleeding risk", "HIGH"},
                {"Metformin", "Alcohol", "Risk of lactic acidosis", "HIGH"},
                {"Simvastatin", "Amiodarone", "Risk of muscle damage (myopathy)", "HIGH"},
                {"Lisinopril", "Potassium", "Dangerous potassium levels (hyperkalemia)", "HIGH"},
                {"Ciprofloxacin", "Antacids", "Reduced antibiotic absorption", "MODERATE"},
                {"Methotrexate", "Ibuprofen", "Increased methotrexate toxicity", "HIGH"},
                {"Digoxin", "Amiodarone", "Increased digoxin toxicity", "HIGH"},
                {"Clopidogrel", "Aspirin", "Increased bleeding risk", "MODERATE"},
                {"Fluoxetine", "Tramadol", "Risk of serotonin syndrome", "HIGH"},
                {"Sertraline", "Tramadol", "Risk of serotonin syndrome", "HIGH"},
                {"Lithium", "Ibuprofen", "Increased lithium toxicity", "HIGH"},
                {"Lithium", "Diclofenac", "Increased lithium toxicity", "HIGH"},
                {"Atorvastatin", "Clarithromycin", "Increased risk of muscle damage", "MODERATE"},
                {"Amlodipine", "Simvastatin", "Increased risk of muscle damage", "MODERATE"},
                {"Metoprolol", "Verapamil", "Risk of heart block and bradycardia", "HIGH"},
                {"Warfarin", "Diclofenac", "Increased bleeding risk", "HIGH"},
                {"Phenytoin", "Fluconazole", "Increased phenytoin toxicity", "MODERATE"},
                {"Carbamazepine", "Erythromycin", "Increased carbamazepine toxicity", "MODERATE"},
                {"Azithromycin", "Antacids", "Reduced antibiotic absorption", "LOW"},
                {"Metformin", "Contrast dye", "Risk of kidney damage", "HIGH"},
                {"ACE inhibitors", "NSAIDs", "Reduced blood pressure control", "MODERATE"},
                {"Diazepam", "Alcohol", "Increased sedation, risk of overdose", "HIGH"},
                {"Codeine", "Alcohol", "Increased sedation and respiratory depression", "HIGH"},
                {"Paracetamol", "Alcohol", "Increased risk of liver damage", "MODERATE"},
                {"Insulin", "Alcohol", "Risk of hypoglycemia", "HIGH"},
                {"Glipizide", "Fluconazole", "Increased risk of hypoglycemia", "MODERATE"},
                {"Theophylline", "Ciprofloxacin", "Increased theophylline toxicity", "HIGH"},
                {"Doxycycline", "Antacids", "Reduced antibiotic absorption", "MODERATE"},
                {"Amoxicillin", "Warfarin", "Increased bleeding risk", "MODERATE"}
        };

        for (String[] data : interactions) {
            MedicineInteraction interaction = new MedicineInteraction();
            interaction.setMedicineA(data[0]);
            interaction.setMedicineB(data[1]);
            interaction.setDescription(data[2]);
            interaction.setSeverity(data[3]);
            repository.save(interaction);
        }

        System.out.println("✅ Medicine interactions seeded: "
                + interactions.length + " pairs");
    }
}