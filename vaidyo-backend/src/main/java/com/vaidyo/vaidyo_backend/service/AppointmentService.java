package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.AppointmentRequest;
import com.vaidyo.vaidyo_backend.dto.AppointmentResponse;
import com.vaidyo.vaidyo_backend.entity.Appointment;
import com.vaidyo.vaidyo_backend.entity.CaretakerPatient;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.AppointmentRepository;
import com.vaidyo.vaidyo_backend.repository.CaretakerPatientRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository
            doctorProfileRepository;
    private final TelegramService telegramService;
    private final CaretakerPatientRepository
            caretakerPatientRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            DoctorProfileRepository doctorProfileRepository,
            TelegramService telegramService,
            CaretakerPatientRepository
                    caretakerPatientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.telegramService = telegramService;
        this.caretakerPatientRepository =
                caretakerPatientRepository;
    }

    // ── Book Appointment ───────────────────────────────────────
    @Transactional
    public AppointmentResponse bookAppointment(
            Long patientId,
            AppointmentRequest request) {

        User patient = userRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found"));

        User doctor = userRepository
                .findById(request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setPatientAddress(
                request.getPatientAddress());
        appointment.setSymptoms(request.getSymptoms());
        appointment.setStatus(
                Appointment.AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);

        // Notify doctor
        if (doctor.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    doctor.getTelegramChatId(),
                    "🏥 <b>New Appointment Request</b>\n\n"
                            + "Patient: <b>"
                            + patient.getFullName() + "</b>\n"
                            + "Symptoms: "
                            + request.getSymptoms() + "\n"
                            + "Address: "
                            + request.getPatientAddress()
                            + "\n\nPlease reply with time and fee."
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "📋 <b>Appointment Booked</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> has booked an appointment with Dr. "
                        + doctor.getFullName() + "\n"
                        + "Symptoms: " + request.getSymptoms()
                        + "\nStatus: PENDING"
        );

        return mapToResponse(appointment);
    }

    // ── Get Patient Appointments ───────────────────────────────
    @Transactional
    public List<AppointmentResponse> getPatientAppointments(
            Long patientId) {
        return appointmentRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Doctor Appointments ────────────────────────────────
    @Transactional
    public List<AppointmentResponse> getDoctorAppointments(
            Long doctorId) {
        return appointmentRepository
                .findByDoctorId(doctorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Doctor Reply ───────────────────────────────────────────
    @Transactional
    public AppointmentResponse doctorReply(
            Long appointmentId,
            String scheduledAt,
            Double fee,
            String notes) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found"));

        appointment.setScheduledAt(
                LocalDateTime.parse(scheduledAt));
        appointment.setFee(fee);
        appointment.setDoctorNotes(notes);
        appointment.setStatus(
                Appointment.AppointmentStatus.REPLIED);

        appointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User doctor = appointment.getDoctor();

        // Notify patient
        if (patient.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    patient.getTelegramChatId(),
                    "✅ <b>Doctor Replied!</b>\n\n"
                            + "Dr. " + doctor.getFullName()
                            + " has replied:\n\n"
                            + "📅 Time: " + scheduledAt + "\n"
                            + "💰 Fee: ₹" + fee + "\n"
                            + "📝 Notes: " + notes
                            + "\n\nPlease accept or reject in app."
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "📅 <b>Doctor Replied to Appointment</b>\n\n"
                        + "Dr. " + doctor.getFullName()
                        + " replied to " + patient.getFullName()
                        + "'s appointment request.\n\n"
                        + "📅 Proposed Time: " + scheduledAt + "\n"
                        + "💰 Fee: ₹" + fee
        );

        return mapToResponse(appointment);
    }

    // ── Patient Accept ─────────────────────────────────────────
    @Transactional
    public AppointmentResponse acceptAppointment(
            Long appointmentId) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found"));

        appointment.setStatus(
                Appointment.AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User doctor = appointment.getDoctor();

        // Notify doctor
        if (doctor.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    doctor.getTelegramChatId(),
                    "🎉 <b>Appointment Confirmed!</b>\n\n"
                            + "Patient <b>"
                            + patient.getFullName()
                            + "</b> accepted your appointment.\n\n"
                            + "📅 " + appointment.getScheduledAt()
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "🎉 <b>Appointment Confirmed!</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> confirmed appointment with Dr. "
                        + doctor.getFullName() + "\n\n"
                        + "📅 Scheduled: "
                        + appointment.getScheduledAt()
                        + "\n💰 Fee: ₹" + appointment.getFee()
        );

        return mapToResponse(appointment);
    }

    // ── Patient Reject ─────────────────────────────────────────
    @Transactional
    public AppointmentResponse rejectAppointment(
            Long appointmentId) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found"));

        appointment.setStatus(
                Appointment.AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User doctor = appointment.getDoctor();

        // Notify doctor
        if (doctor.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    doctor.getTelegramChatId(),
                    "❌ <b>Appointment Rejected</b>\n\n"
                            + "Patient <b>"
                            + patient.getFullName()
                            + "</b> rejected the appointment."
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "❌ <b>Appointment Rejected</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b> rejected appointment with Dr. "
                        + doctor.getFullName()
        );

        return mapToResponse(appointment);
    }

    // ── Mark Complete ──────────────────────────────────────────
    @Transactional
    public AppointmentResponse completeAppointment(
            Long appointmentId) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found"));

        appointment.setStatus(
                Appointment.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        User patient = appointment.getPatient();
        User doctor = appointment.getDoctor();

        // Notify patient
        if (patient.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    patient.getTelegramChatId(),
                    "✅ <b>Appointment Completed!</b>\n\n"
                            + "Your appointment with Dr. "
                            + doctor.getFullName()
                            + " is completed.\n\n"
                            + "Please rate in the app!"
            );
        }

        // Notify caretakers
        notifyCaretakers(patient,
                "✅ <b>Appointment Completed!</b>\n\n"
                        + "Patient <b>" + patient.getFullName()
                        + "</b>'s appointment with Dr. "
                        + doctor.getFullName()
                        + " has been completed!"
        );

        return mapToResponse(appointment);
    }

    // ── Notify all caretakers ──────────────────────────────────
    private void notifyCaretakers(User patient,
                                  String message) {
        List<CaretakerPatient> caretakers =
                caretakerPatientRepository
                        .findByPatientId(patient.getId());

        for (CaretakerPatient cp : caretakers) {
            User caretaker = cp.getCaretaker();
            if (caretaker.getTelegramChatId() != null) {
                telegramService.sendMessage(
                        caretaker.getTelegramChatId(),
                        message);
            }
        }
    }

    // ── Map to response ────────────────────────────────────────
    @Transactional
    public AppointmentResponse mapToResponse(
            Appointment appointment) {

        AppointmentResponse response =
                new AppointmentResponse();
        response.setId(appointment.getId());

        User patient = appointment.getPatient();
        response.setPatientId(patient.getId());
        response.setPatientName(patient.getFullName());

        User doctor = appointment.getDoctor();
        response.setDoctorId(doctor.getId());
        response.setDoctorName(doctor.getFullName());

        doctorProfileRepository.findByUserId(doctor.getId())
                .ifPresent(profile ->
                        response.setSpeciality(
                                profile.getSpeciality()
                                        .name()));

        response.setScheduledAt(
                appointment.getScheduledAt());
        response.setPatientAddress(
                appointment.getPatientAddress());
        response.setSymptoms(appointment.getSymptoms());
        response.setFee(appointment.getFee());
        response.setDoctorNotes(
                appointment.getDoctorNotes());
        response.setStatus(
                appointment.getStatus().name());
        response.setCreatedAt(appointment.getCreatedAt());

        return response;
    }
}