package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.SosRequest;
import com.vaidyo.vaidyo_backend.dto.SosResponse;
import com.vaidyo.vaidyo_backend.entity.EmergencyContact;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.EmergencyContactRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SosService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserRepository userRepository;
    private final TelegramService telegramService;
    private final MessageService messageService;

    public SosService(EmergencyContactRepository emergencyContactRepository,
                      UserRepository userRepository,
                      TelegramService telegramService,
                      MessageService messageService) {
        this.emergencyContactRepository = emergencyContactRepository;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
        this.messageService = messageService;
    }

    private User getCurrentPatient(Authentication authentication) {
        String mobileNumber = authentication.getName();
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public SosResponse triggerSos(Authentication authentication, SosRequest request) {
        User patient = getCurrentPatient(authentication);

        List<EmergencyContact> contacts =
                emergencyContactRepository.findByPatient_IdOrderByPriorityOrderAsc(patient.getId());

        if (contacts.isEmpty()) {
            throw new IllegalStateException(
                    "No emergency contacts configured. Please add at least one emergency contact before using SOS.");
        }

        String locationLink = buildLocationLink(request.getLatitude(), request.getLongitude());
        String lang = patient.getPreferredLanguage();

        String title = messageService.get("sos.alert.title", lang);
        String body = messageService.get("sos.alert.body", lang, patient.getFullName(), locationLink);
        String alertMessage = "<b>" + title + "</b>\n\n" + body;

        List<String> notified = new ArrayList<>();
        List<SosResponse.UnlinkedContact> toCallManually = new ArrayList<>();

        for (EmergencyContact contact : contacts) {
            if (contact.getTelegramChatId() != null && !contact.getTelegramChatId().isBlank()) {
                telegramService.sendMessage(contact.getTelegramChatId(), alertMessage);
                notified.add(contact.getContactName());
            } else {
                toCallManually.add(new SosResponse.UnlinkedContact(
                        contact.getContactName(), contact.getPhoneNumber()));
            }
        }

        return new SosResponse(
                patient.getFullName(),
                locationLink,
                contacts.size(),
                notified,
                toCallManually,
                LocalDateTime.now()
        );
    }

    private String buildLocationLink(Double latitude, Double longitude) {
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }
}