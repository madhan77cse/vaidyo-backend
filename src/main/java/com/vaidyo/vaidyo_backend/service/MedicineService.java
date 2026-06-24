package com.vaidyo.vaidyo_backend.service;

<<<<<<< HEAD
import com.vaidyo.vaidyo_backend.dto.MedicineRequest;
import com.vaidyo.vaidyo_backend.dto.MedicineResponse;
import com.vaidyo.vaidyo_backend.entity.Medicine;
import com.vaidyo.vaidyo_backend.entity.MedicineLog;
import com.vaidyo.vaidyo_backend.entity.User;
=======
import com.vaidyo.vaidyo_backend.dto.MedicineInteractionResponse;
import com.vaidyo.vaidyo_backend.dto.MedicineRequest;
import com.vaidyo.vaidyo_backend.dto.MedicineResponse;
import com.vaidyo.vaidyo_backend.entity.Medicine;
import com.vaidyo.vaidyo_backend.entity.MedicineInteraction;
import com.vaidyo.vaidyo_backend.entity.MedicineLog;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.MedicineInteractionRepository;
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
import com.vaidyo.vaidyo_backend.repository.MedicineLogRepository;
import com.vaidyo.vaidyo_backend.repository.MedicineRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineLogRepository medicineLogRepository;
    private final UserRepository userRepository;
<<<<<<< HEAD

    public MedicineService(MedicineRepository medicineRepository,
                           MedicineLogRepository medicineLogRepository,
                           UserRepository userRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineLogRepository = medicineLogRepository;
        this.userRepository = userRepository;
    }

    // ── Add Medicine ───────────────────────────────────────────
    public MedicineResponse addMedicine(Long patientId,
                                        MedicineRequest request) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));
=======
    private final MedicineInteractionRepository interactionRepository;

    public MedicineService(MedicineRepository medicineRepository,
                           MedicineLogRepository medicineLogRepository,
                           UserRepository userRepository,
                           MedicineInteractionRepository interactionRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineLogRepository = medicineLogRepository;
        this.userRepository = userRepository;
        this.interactionRepository = interactionRepository;
    }

    public Map<String, Object> addMedicine(Long patientId, MedicineRequest request) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32

        Medicine medicine = new Medicine();
        medicine.setPatient(patient);
        medicine.setMedicineName(request.getMedicineName());
        medicine.setDosage(request.getDosage());
        medicine.setFrequency(request.getFrequency());
        medicine.setNotes(request.getNotes());
<<<<<<< HEAD

        if (request.getReminderTime() != null
                && !request.getReminderTime().isEmpty()) {
            medicine.setReminderTime(
                    LocalTime.parse(request.getReminderTime()));
        }

        medicineRepository.save(medicine);
        return mapToResponse(medicine);
    }

    // ── Get All Medicines for Patient ──────────────────────────
    public List<MedicineResponse> getMyMedicines(Long patientId) {
        return medicineRepository
                .findByPatientIdAndStatus(
                        patientId,
                        Medicine.MedicineStatus.ACTIVE)
=======
        medicine.setExpiryDate(request.getExpiryDate());

        if (request.getReminderTime() != null && !request.getReminderTime().isEmpty()) {
            medicine.setReminderTime(LocalTime.parse(request.getReminderTime()));
        }

        medicineRepository.save(medicine);

        // ── Check interactions with existing medicines ─────────
        List<Medicine> existingMedicines = medicineRepository
                .findByPatientIdAndStatus(patientId, Medicine.MedicineStatus.ACTIVE);

        List<MedicineInteractionResponse> warnings = new ArrayList<>();

        for (Medicine existing : existingMedicines) {
            if (existing.getId().equals(medicine.getId())) continue;

            List<MedicineInteraction> found = interactionRepository
                    .findInteraction(request.getMedicineName(),
                            existing.getMedicineName());

            for (MedicineInteraction interaction : found) {
                warnings.add(new MedicineInteractionResponse(
                        request.getMedicineName(),
                        existing.getMedicineName(),
                        interaction.getDescription(),
                        interaction.getSeverity()
                ));
            }
        }

        MedicineResponse medicineResponse = mapToResponse(medicine);

        return Map.of(
                "medicine", medicineResponse,
                "interactions", warnings,
                "hasInteractions", !warnings.isEmpty()
        );
    }

    public List<MedicineResponse> getMyMedicines(Long patientId) {
        return medicineRepository
                .findByPatientIdAndStatus(patientId, Medicine.MedicineStatus.ACTIVE)
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

<<<<<<< HEAD
    // ── Mark Medicine as Taken ─────────────────────────────────
    public String markAsTaken(Long medicineId, Long patientId) {
        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException("Medicine not found"));

        User patient = userRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));
=======
    public String markAsTaken(Long medicineId, Long patientId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32

        MedicineLog log = new MedicineLog();
        log.setMedicine(medicine);
        log.setPatient(patient);
        log.setStatus(MedicineLog.LogStatus.TAKEN);
        log.setTakenAt(LocalDateTime.now());
        log.setScheduledTime(LocalDateTime.now());

        medicineLogRepository.save(log);
        return "Medicine marked as taken!";
    }

    @Transactional
    public List<MedicineLog> getMedicineLogs(Long patientId) {
<<<<<<< HEAD
        List<MedicineLog> logs = medicineLogRepository
                .findByPatientId(patientId);
        // Force load lazy references
        logs.forEach(log -> {
            if (log.getMedicine() != null) {
                log.getMedicine().getMedicineName();
            }
            if (log.getPatient() != null) {
                log.getPatient().getFullName();
            }
=======
        List<MedicineLog> logs = medicineLogRepository.findByPatientId(patientId);
        logs.forEach(log -> {
            if (log.getMedicine() != null) log.getMedicine().getMedicineName();
            if (log.getPatient() != null) log.getPatient().getFullName();
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
        });
        return logs;
    }

<<<<<<< HEAD
    // ── Delete Medicine ────────────────────────────────────────
    public String deleteMedicine(Long medicineId) {
        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException("Medicine not found"));

=======
    public String deleteMedicine(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
        medicine.setStatus(Medicine.MedicineStatus.INACTIVE);
        medicineRepository.save(medicine);
        return "Medicine removed successfully!";
    }

<<<<<<< HEAD
    // ── Update Photo URL ───────────────────────────────────────
    public void updatePhotoUrl(Long medicineId, String photoUrl) {
        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException("Medicine not found"));
=======
    public void updatePhotoUrl(Long medicineId, String photoUrl) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
        medicine.setPhotoUrl(photoUrl);
        medicineRepository.save(medicine);
    }

<<<<<<< HEAD
    // ── Helper: Map entity to response ────────────────────────
=======
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
    public MedicineResponse mapToResponse(Medicine medicine) {
        MedicineResponse response = new MedicineResponse();
        response.setId(medicine.getId());
        response.setMedicineName(medicine.getMedicineName());
        response.setDosage(medicine.getDosage());
        response.setFrequency(medicine.getFrequency());
        response.setReminderTime(medicine.getReminderTime());
        response.setPhotoUrl(medicine.getPhotoUrl());
<<<<<<< HEAD
=======
        response.setExpiryDate(medicine.getExpiryDate());
>>>>>>> ea6533cf9990bb8976b137e975efd94762a22b32
        response.setStatus(medicine.getStatus().name());
        response.setNotes(medicine.getNotes());
        response.setCreatedAt(medicine.getCreatedAt());
        return response;
    }
}