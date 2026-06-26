package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.entity.MedicineLog;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.MedicineLogRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramDoseService {

    private final MedicineLogRepository medicineLogRepository;
    private final UserRepository userRepository;

    public TelegramDoseService(
            MedicineLogRepository medicineLogRepository,
            UserRepository userRepository) {
        this.medicineLogRepository = medicineLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean markLatestDoseTaken(String chatId) {
        // Find the user by telegram chat ID
        Optional<User> userOpt = userRepository
                .findByTelegramChatId(chatId);

        if (userOpt.isEmpty()) return false;

        User patient = userOpt.get();

        // Find most recent PENDING log for this patient
        List<MedicineLog> pendingLogs = medicineLogRepository
                .findByPatientId(patient.getId())
                .stream()
                .filter(log -> log.getStatus()
                        == MedicineLog.LogStatus.PENDING)
                .sorted(Comparator.comparing(
                        MedicineLog::getScheduledTime).reversed())
                .toList();

        if (pendingLogs.isEmpty()) return false;

        MedicineLog latest = pendingLogs.get(0);
        latest.setStatus(MedicineLog.LogStatus.TAKEN);
        latest.setTakenAt(LocalDateTime.now());
        medicineLogRepository.save(latest);

        return true;
    }
}