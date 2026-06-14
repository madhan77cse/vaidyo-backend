package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.entity.CaretakerPatient;
import com.vaidyo.vaidyo_backend.entity.Medicine;
import com.vaidyo.vaidyo_backend.entity.MedicineLog;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.CaretakerPatientRepository;
import com.vaidyo.vaidyo_backend.repository.MedicineLogRepository;
import com.vaidyo.vaidyo_backend.repository.MedicineRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MedicineReminderScheduler {

    private final MedicineRepository medicineRepository;
    private final MedicineLogRepository medicineLogRepository;
    private final UserRepository userRepository;
    private final CaretakerPatientRepository caretakerPatientRepository;
    private final TelegramService telegramService;

    public MedicineReminderScheduler(
            MedicineRepository medicineRepository,
            MedicineLogRepository medicineLogRepository,
            UserRepository userRepository,
            CaretakerPatientRepository caretakerPatientRepository,
            TelegramService telegramService) {
        this.medicineRepository = medicineRepository;
        this.medicineLogRepository = medicineLogRepository;
        this.userRepository = userRepository;
        this.caretakerPatientRepository = caretakerPatientRepository;
        this.telegramService = telegramService;
    }

    // ── Runs every minute ──────────────────────────────────────
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void sendMedicineReminders() {

        LocalTime now = LocalTime.now()
                .withSecond(0).withNano(0);

        System.out.println("⏰ Scheduler running at: " + now);

        List<Medicine> allMedicines =
                medicineRepository.findAll();

        System.out.println("💊 Total medicines: "
                + allMedicines.size());

        for (Medicine medicine : allMedicines) {

            if (medicine.getStatus() !=
                    Medicine.MedicineStatus.ACTIVE) continue;

            if (medicine.getReminderTime() == null) continue;

            LocalTime reminderTime = medicine.getReminderTime()
                    .withSecond(0).withNano(0);

            System.out.println("🔔 Checking: "
                    + medicine.getMedicineName()
                    + " reminderTime: " + reminderTime
                    + " now: " + now
                    + " match: " + reminderTime.equals(now));

            if (!reminderTime.equals(now)) continue;

            User patient = medicine.getPatient();

            System.out.println("👤 Patient: "
                    + patient.getFullName()
                    + " telegramId: "
                    + patient.getTelegramChatId());

            if (patient.getTelegramChatId() != null) {
                telegramService.sendMedicineReminder(
                        patient.getTelegramChatId(),
                        patient.getFullName(),
                        medicine.getMedicineName(),
                        medicine.getDosage()
                );
                System.out.println("✅ Reminder sent to: "
                        + patient.getFullName());
            } else {
                System.out.println("❌ No telegram linked!");
            }

            LocalDateTime scheduledTime =
                    LocalDateTime.of(LocalDate.now(),
                            medicine.getReminderTime());

            MedicineLog log = new MedicineLog();
            log.setMedicine(medicine);
            log.setPatient(patient);
            log.setStatus(MedicineLog.LogStatus.PENDING);
            log.setScheduledTime(scheduledTime);
            medicineLogRepository.save(log);
        }
    }

    // ── Runs every 30 minutes ──────────────────────────────────
    @Scheduled(fixedRate = 1800000)
    public void checkMissedMedicines() {

        LocalDateTime thirtyMinsAgo =
                LocalDateTime.now().minusMinutes(30);

        List<MedicineLog> allPending =
                medicineLogRepository.findAll()
                        .stream()
                        .filter(log -> log.getStatus()
                                == MedicineLog.LogStatus.PENDING)
                        .filter(log -> log.getScheduledTime()
                                .isBefore(thirtyMinsAgo))
                        .toList();

        for (MedicineLog log : allPending) {

            log.setStatus(MedicineLog.LogStatus.MISSED);
            medicineLogRepository.save(log);

            User patient = log.getPatient();
            List<CaretakerPatient> caretakers =
                    caretakerPatientRepository
                            .findByPatientId(patient.getId());

            for (CaretakerPatient cp : caretakers) {
                User caretaker = cp.getCaretaker();
                if (caretaker.getTelegramChatId() != null) {
                    telegramService.sendMissedAlert(
                            caretaker.getTelegramChatId(),
                            patient.getFullName(),
                            log.getMedicine().getMedicineName()
                    );
                }
            }
        }
    }
}