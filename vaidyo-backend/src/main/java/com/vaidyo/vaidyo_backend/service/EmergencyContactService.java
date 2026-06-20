package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.EmergencyContactRequest;
import com.vaidyo.vaidyo_backend.dto.EmergencyContactResponse;
import com.vaidyo.vaidyo_backend.entity.EmergencyContact;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.EmergencyContactRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyContactService {

    private static final int MAX_CONTACTS = 5;

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserRepository userRepository;

    public EmergencyContactService(EmergencyContactRepository emergencyContactRepository,
                                   UserRepository userRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentPatient(Authentication authentication) {
        String mobileNumber = authentication.getName();
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public EmergencyContactResponse addContact(Authentication authentication, EmergencyContactRequest request) {
        User patient = getCurrentPatient(authentication);

        long existingCount = emergencyContactRepository.countByPatient_Id(patient.getId());
        if (existingCount >= MAX_CONTACTS) {
            throw new IllegalStateException("Maximum of " + MAX_CONTACTS + " emergency contacts allowed");
        }

        EmergencyContact contact = new EmergencyContact();
        contact.setPatient(patient);
        contact.setContactName(request.getContactName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setRelationship(request.getRelationship());
        contact.setPriorityOrder(
                request.getPriorityOrder() != null ? request.getPriorityOrder() : (int) existingCount + 1
        );

        EmergencyContact saved = emergencyContactRepository.save(contact);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<EmergencyContactResponse> getContacts(Authentication authentication) {
        User patient = getCurrentPatient(authentication);
        return emergencyContactRepository.findByPatient_IdOrderByPriorityOrderAsc(patient.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmergencyContactResponse updateContact(Authentication authentication, Long contactId,
                                                  EmergencyContactRequest request) {
        User patient = getCurrentPatient(authentication);
        EmergencyContact contact = emergencyContactRepository.findByIdAndPatient_Id(contactId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));

        contact.setContactName(request.getContactName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setRelationship(request.getRelationship());
        if (request.getPriorityOrder() != null) {
            contact.setPriorityOrder(request.getPriorityOrder());
        }

        EmergencyContact saved = emergencyContactRepository.save(contact);
        return toResponse(saved);
    }

    @Transactional
    public void deleteContact(Authentication authentication, Long contactId) {
        User patient = getCurrentPatient(authentication);
        EmergencyContact contact = emergencyContactRepository.findByIdAndPatient_Id(contactId, patient.getId())
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
        emergencyContactRepository.delete(contact);
    }

    private EmergencyContactResponse toResponse(EmergencyContact contact) {
        return new EmergencyContactResponse(
                contact.getId(),
                contact.getContactName(),
                contact.getPhoneNumber(),
                contact.getRelationship(),
                contact.getPriorityOrder(),
                contact.getTelegramChatId() != null,
                contact.getCreatedAt()
        );
    }
}