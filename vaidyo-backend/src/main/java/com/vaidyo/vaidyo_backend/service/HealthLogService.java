package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.HealthLogRequest;
import com.vaidyo.vaidyo_backend.dto.HealthLogResponse;
import com.vaidyo.vaidyo_backend.entity.CaretakerPatient;
import com.vaidyo.vaidyo_backend.entity.HealthLog;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.CaretakerPatientRepository;
import com.vaidyo.vaidyo_backend.repository.HealthLogRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HealthLogService {

    private final HealthLogRepository healthLogRepository;
    private final UserRepository userRepository;
    private final TelegramService telegramService;
    private final CaretakerPatientRepository
            caretakerPatientRepository;

    public HealthLogService(
            HealthLogRepository healthLogRepository,
            UserRepository userRepository,
            TelegramService telegramService,
            CaretakerPatientRepository
                    caretakerPatientRepository) {
        this.healthLogRepository = healthLogRepository;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
        this.caretakerPatientRepository =
                caretakerPatientRepository;
    }

    // ── Add Health Log ─────────────────────────────────────────
    public HealthLogResponse addHealthLog(Long patientId,
                                          HealthLogRequest request) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        HealthLog log = new HealthLog();
        log.setPatient(patient);
        log.setBpSystolic(request.getBpSystolic());
        log.setBpDiastolic(request.getBpDiastolic());
        log.setSugarLevel(request.getSugarLevel());
        log.setWeight(request.getWeight());
        log.setPulseRate(request.getPulseRate());
        log.setTemperature(request.getTemperature());
        log.setNotes(request.getNotes());

        HealthLog.AlertStatus alertStatus =
                checkAlertStatus(request);
        log.setAlertStatus(alertStatus);

        healthLogRepository.save(log);

        if (alertStatus != HealthLog.AlertStatus.NORMAL) {
            sendHealthAlert(patient, log, alertStatus);
        }

        return mapToResponse(log);
    }

    // ── Get Health History ─────────────────────────────────────
    @Transactional(readOnly = true)
    public List<HealthLogResponse> getHealthHistory(
            Long patientId) {
        return healthLogRepository
                .findByPatientIdOrderByLoggedAtDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Latest Reading ─────────────────────────────────────
    @Transactional(readOnly = true)
    public HealthLogResponse getLatestReading(Long patientId) {
        return healthLogRepository
                .findFirstByPatientIdOrderByLoggedAtDesc(
                        patientId)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No health logs found"));
    }

    // ── Check Alert Status ─────────────────────────────────────
    private HealthLog.AlertStatus checkAlertStatus(
            HealthLogRequest request) {

        if (request.getBpSystolic() != null) {
            if (request.getBpSystolic() >= 180 ||
                    request.getBpSystolic() <= 80) {
                return HealthLog.AlertStatus.CRITICAL;
            }
            if (request.getBpSystolic() >= 140 ||
                    request.getBpSystolic() <= 90) {
                return HealthLog.AlertStatus.WARNING;
            }
        }

        if (request.getSugarLevel() != null) {
            if (request.getSugarLevel() >= 400 ||
                    request.getSugarLevel() <= 50) {
                return HealthLog.AlertStatus.CRITICAL;
            }
            if (request.getSugarLevel() >= 200 ||
                    request.getSugarLevel() <= 70) {
                return HealthLog.AlertStatus.WARNING;
            }
        }

        if (request.getPulseRate() != null) {
            if (request.getPulseRate() >= 150 ||
                    request.getPulseRate() <= 40) {
                return HealthLog.AlertStatus.CRITICAL;
            }
            if (request.getPulseRate() >= 100 ||
                    request.getPulseRate() <= 60) {
                return HealthLog.AlertStatus.WARNING;
            }
        }

        if (request.getTemperature() != null) {
            if (request.getTemperature() >= 40.0 ||
                    request.getTemperature() <= 35.0) {
                return HealthLog.AlertStatus.CRITICAL;
            }
            if (request.getTemperature() >= 38.0) {
                return HealthLog.AlertStatus.WARNING;
            }
        }

        return HealthLog.AlertStatus.NORMAL;
    }

    // ── Send Health Alert ──────────────────────────────────────
    private void sendHealthAlert(User patient,
                                 HealthLog log,
                                 HealthLog.AlertStatus status) {
        String emoji =
                status == HealthLog.AlertStatus.CRITICAL
                        ? "🚨" : "⚠️";

        String message = emoji
                + " <b>Health Alert</b>\n\n"
                + "Patient: <b>"
                + patient.getFullName() + "</b>\n\n";

        if (log.getBpSystolic() != null) {
            message += "🩸 BP: " + log.getBpSystolic()
                    + "/" + log.getBpDiastolic()
                    + " mmHg\n";
        }
        if (log.getSugarLevel() != null) {
            message += "🍬 Sugar: "
                    + log.getSugarLevel() + " mg/dL\n";
        }
        if (log.getPulseRate() != null) {
            message += "💓 Pulse: "
                    + log.getPulseRate() + " bpm\n";
        }
        if (log.getTemperature() != null) {
            message += "🌡️ Temp: "
                    + log.getTemperature() + "°C\n";
        }

        message += "\nStatus: <b>" + status.name()
                + "</b> - Please check immediately!";

        // Send to patient
        if (patient.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    patient.getTelegramChatId(), message);
        }

        // Send to all caretakers
        List<CaretakerPatient> caretakers =
                caretakerPatientRepository
                        .findByPatientId(patient.getId());

        for (CaretakerPatient cp : caretakers) {
            User caretaker = cp.getCaretaker();
            if (caretaker.getTelegramChatId() != null) {
                telegramService.sendMessage(
                        caretaker.getTelegramChatId(),
                        "👨‍👩‍👦 <b>Patient Health Alert</b>\n\n"
                                + message);
            }
        }
    }

    // ── Map to response ────────────────────────────────────────
    private HealthLogResponse mapToResponse(HealthLog log) {
        HealthLogResponse response = new HealthLogResponse();
        response.setId(log.getId());
        response.setBpSystolic(log.getBpSystolic());
        response.setBpDiastolic(log.getBpDiastolic());
        response.setSugarLevel(log.getSugarLevel());
        response.setWeight(log.getWeight());
        response.setPulseRate(log.getPulseRate());
        response.setTemperature(log.getTemperature());
        response.setNotes(log.getNotes());
        response.setAlertStatus(
                log.getAlertStatus().name());
        response.setLoggedAt(log.getLoggedAt());
        return response;
    }
}