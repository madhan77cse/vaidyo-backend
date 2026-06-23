package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.entity.Medicine;
import com.vaidyo.vaidyo_backend.repository.MedicineRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MedicineExpiryScheduler {

    private final MedicineRepository medicineRepository;
    private final TelegramService telegramService;

    public MedicineExpiryScheduler(MedicineRepository medicineRepository,
                                   TelegramService telegramService) {
        this.medicineRepository = medicineRepository;
        this.telegramService = telegramService;
    }

    // Runs every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void checkExpiringMedicines() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        List<Medicine> expiringMedicines =
                medicineRepository.findByExpiryDateBetween(today, sevenDaysLater);

        for (Medicine medicine : expiringMedicines) {
            String chatId = medicine.getPatient().getTelegramChatId();

            if (chatId != null && !chatId.isEmpty()) {
                String message = "⚠️ <b>Medicine Expiry Alert!</b>\n\n"
                        + "Hi <b>" + medicine.getPatient().getFullName() + "</b>!\n\n"
                        + "Your medicine <b>" + medicine.getMedicineName() + "</b> "
                        + "is expiring on <b>" + medicine.getExpiryDate() + "</b>.\n\n"
                        + "Please renew your prescription soon. 🏥";

                telegramService.sendMessage(chatId, message);
            }
        }
    }
}