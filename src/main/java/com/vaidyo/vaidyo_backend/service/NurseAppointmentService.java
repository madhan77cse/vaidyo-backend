package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.NurseAppointmentRequest;
import com.vaidyo.vaidyo_backend.dto.NurseAppointmentResponse;
import com.vaidyo.vaidyo_backend.entity.NurseAppointment;
import com.vaidyo.vaidyo_backend.entity.NurseProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.CaretakerPatientRepository;
import com.vaidyo.vaidyo_backend.entity.CaretakerPatient;
import com.vaidyo.vaidyo_backend.repository.NurseAppointmentRepository;
import com.vaidyo.vaidyo_backend.repository.NurseProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NurseAppointmentService {

    private final NurseAppointmentRepository nurseAppointmentRepository;
    private final UserRepository userRepository;
    private final NurseProfileRepository nurseProfileRepository;
    private final TelegramService telegramService;
    private final CaretakerPatientRepository caretakerPatientRepository;

    public NurseAppointmentService(
            NurseAppointmentRepository nurseAppointmentRepository,
            UserRepository userRepository,
            NurseProfileRepository nurseProfileRepository,
            TelegramService telegramService,
            CaretakerPatientRepository caretakerPatientRepository) {
        this.nurseAppointmentRepository = nurseAppointmentRepository;
        this.userRepository = userRepository;
        this.nurseProfileRepository = nurseProfileRepository;
        this.telegramService = telegramService;
        this.caretakerPatientRepository = caretakerPatientRepository;
    }

    // ── Book nurse appointment ─────────────────────────────────
    @Transactional
    public NurseAppointmentResponse bookAppointment(
            Long patientId, NurseAppointmentRequest request) {

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User nurse = userRepository.findById(request.getNurseId())
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        // Only allow booking with APPROVED nurses
        NurseProfile nurseProfile = nurseProfileRepository
                .findByUserId(nurse.getId())
                .orElseThrow(() -> new RuntimeException("Nurse profile not found"));

        if (nurseProfile.getVerificationStatus() != NurseProfile.VerificationStatus.APPROVED) {
            throw new RuntimeException("This nurse is not yet approved for bookings");
        }

        NurseAppointment appointment = new NurseAppointment();
        appointment.setPatient(patient);
        appointment.setNurse(nurse);
        appointment.setPatientAddress(request.getPatientAddress());
        appointment.setSymptoms(request.getSymptoms());
        appointment.setStatus(NurseAppointment.NurseAppointmentStatus.PENDING);

        nurseAppointmentRepository.save(appointment);

        // Notify nurse via Telegram
        if (nurse.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    nurse.getTelegramChatId(),
                    "🏥 <b>New Appointment Request</b>\n\n"
                            + "Patient: <b>" + patient.getFullName() + "</b>\n"
                            + "Symptoms: " + request.getSymptoms() + "\n"
                            + "Address: " + request.getPatientAddress()
                            + "\n\nPlease reply with time and fee."
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "📋 <b>Nurse Appointment Booked</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> has booked a nurse appointment with "
                        + nurse.getFullName() + "\n"
                        + "Symptoms: " + request.getSymptoms()
                        + "\nStatus: PENDING"
        );

        return mapToResponse(appointment);
    }

    // ── Get patient's nurse appointments ──────────────────────
    @Transactional
    public List<NurseAppointmentResponse> getPatientAppointments(Long patientId) {
        return nurseAppointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Get nurse's appointments ───────────────────────────────
    @Transactional
    public List<NurseAppointmentResponse> getNurseAppointments(Long nurseId) {
        return nurseAppointmentRepository.findByNurseId(nurseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Nurse replies with time + fee ──────────────────────────
    @Transactional
    public NurseAppointmentResponse nurseReply(
            Long appointmentId, String scheduledAt,
            Double fee, String notes) {

        NurseAppointment appointment = nurseAppointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setScheduledAt(LocalDateTime.parse(scheduledAt));
        appointment.setFee(fee);
        appointment.setNurseNotes(notes);
        appointment.setStatus(NurseAppointment.NurseAppointmentStatus.REPLIED);
        nurseAppointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User nurse = appointment.getNurse();

        // Notify patient
        if (patient.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    patient.getTelegramChatId(),
                    "✅ <b>Nurse Replied!</b>\n\n"
                            + nurse.getFullName() + " has replied:\n\n"
                            + "📅 Time: " + scheduledAt + "\n"
                            + "💰 Fee: ₹" + fee + "\n"
                            + "📝 Notes: " + notes
                            + "\n\nPlease accept or reject in app."
            );
        }

        notifyCaretakers(patient,
                "📅 <b>Nurse Replied to Appointment</b>\n\n"
                        + nurse.getFullName() + " replied to "
                        + patient.getFullName() + "'s request.\n\n"
                        + "📅 Time: " + scheduledAt + "\n"
                        + "💰 Fee: ₹" + fee
        );

        return mapToResponse(appointment);
    }

    // ── Patient accepts ────────────────────────────────────────
    @Transactional
    public NurseAppointmentResponse acceptAppointment(Long appointmentId) {
        NurseAppointment appointment = nurseAppointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(NurseAppointment.NurseAppointmentStatus.CONFIRMED);
        nurseAppointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User nurse = appointment.getNurse();

        if (nurse.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    nurse.getTelegramChatId(),
                    "🎉 <b>Appointment Confirmed!</b>\n\n"
                            + "Patient <b>" + patient.getFullName()
                            + "</b> accepted your appointment.\n\n"
                            + "📅 " + appointment.getScheduledAt()
            );
        }

        notifyCaretakers(patient,
                "🎉 <b>Nurse Appointment Confirmed!</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> confirmed appointment with "
                        + nurse.getFullName() + "\n\n"
                        + "📅 Scheduled: " + appointment.getScheduledAt()
                        + "\n💰 Fee: ₹" + appointment.getFee()
        );

        return mapToResponse(appointment);
    }

    // ── Patient rejects ────────────────────────────────────────
    @Transactional
    public NurseAppointmentResponse rejectAppointment(Long appointmentId) {
        NurseAppointment appointment = nurseAppointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(NurseAppointment.NurseAppointmentStatus.REJECTED);
        nurseAppointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User nurse = appointment.getNurse();

        if (nurse.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    nurse.getTelegramChatId(),
                    "❌ <b>Appointment Rejected</b>\n\n"
                            + "Patient <b>" + patient.getFullName()
                            + "</b> rejected the appointment."
            );
        }

        notifyCaretakers(patient,
                "❌ <b>Nurse Appointment Rejected</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> rejected appointment with "
                        + nurse.getFullName()
        );

        return mapToResponse(appointment);
    }

    // ── Nurse marks complete ───────────────────────────────────
    @Transactional
    public NurseAppointmentResponse completeAppointment(Long appointmentId) {
        NurseAppointment appointment = nurseAppointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(NurseAppointment.NurseAppointmentStatus.COMPLETED);
        nurseAppointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User nurse = appointment.getNurse();

        if (patient.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    patient.getTelegramChatId(),
                    "✅ <b>Nurse Appointment Completed!</b>\n\n"
                            + "Your appointment with "
                            + nurse.getFullName() + " is completed!"
            );
        }

        notifyCaretakers(patient,
                "✅ <b>Nurse Appointment Completed!</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b>'s appointment with "
                        + nurse.getFullName() + " has been completed!"
        );

        return mapToResponse(appointment);
    }

    // ── Notify caretakers ──────────────────────────────────────
    private void notifyCaretakers(User patient, String message) {
        List<CaretakerPatient> caretakers =
                caretakerPatientRepository.findByPatientId(patient.getId());
        for (CaretakerPatient cp : caretakers) {
            User caretaker = cp.getCaretaker();
            if (caretaker.getTelegramChatId() != null) {
                telegramService.sendMessage(caretaker.getTelegramChatId(), message);
            }
        }
    }

    // ── Map to response ────────────────────────────────────────
    private NurseAppointmentResponse mapToResponse(NurseAppointment appointment) {
        NurseAppointmentResponse response = new NurseAppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatient().getId());
        response.setPatientName(appointment.getPatient().getFullName());
        response.setNurseId(appointment.getNurse().getId());
        response.setNurseName(appointment.getNurse().getFullName());
        response.setScheduledAt(appointment.getScheduledAt());
        response.setPatientAddress(appointment.getPatientAddress());
        response.setSymptoms(appointment.getSymptoms());
        response.setFee(appointment.getFee());
        response.setNurseNotes(appointment.getNurseNotes());
        response.setStatus(appointment.getStatus().name());
        response.setCreatedAt(appointment.getCreatedAt());
        return response;
    }
}