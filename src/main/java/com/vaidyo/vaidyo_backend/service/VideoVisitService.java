package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.VideoVisitResponse;
import com.vaidyo.vaidyo_backend.entity.Appointment;
import com.vaidyo.vaidyo_backend.entity.VideoVisit;
import com.vaidyo.vaidyo_backend.repository.AppointmentRepository;
import com.vaidyo.vaidyo_backend.repository.VideoVisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoVisitService {

    private final VideoVisitRepository videoVisitRepository;
    private final AppointmentRepository appointmentRepository;

    public VideoVisitService(VideoVisitRepository videoVisitRepository,
                             AppointmentRepository appointmentRepository) {
        this.videoVisitRepository = videoVisitRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates a Jitsi room link for the given appointment if one doesn't already exist.
     * Idempotent: calling this again for the same appointment just returns the existing link.
     */
    @Transactional
    public VideoVisitResponse createOrGetVideoVisit(Long appointmentId) {
        return videoVisitRepository.findByAppointment_Id(appointmentId)
                .map(this::toResponse)
                .orElseGet(() -> {
                    Appointment appointment = appointmentRepository.findById(appointmentId)
                            .orElseThrow(() -> new RuntimeException("Appointment not found"));

                    VideoVisit videoVisit = new VideoVisit();
                    videoVisit.setAppointment(appointment);
                    videoVisit.setRoomUrl("https://meet.jit.si/vaidyo-" + appointmentId);

                    VideoVisit saved = videoVisitRepository.save(videoVisit);
                    return toResponse(saved);
                });
    }

    @Transactional(readOnly = true)
    public VideoVisitResponse getVideoVisit(Long appointmentId) {
        VideoVisit videoVisit = videoVisitRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new RuntimeException(
                        "No video visit found for this appointment. Ask the doctor to enable online visit first."));
        return toResponse(videoVisit);
    }

    private VideoVisitResponse toResponse(VideoVisit videoVisit) {
        return new VideoVisitResponse(
                videoVisit.getAppointment().getId(),
                videoVisit.getRoomUrl(),
                videoVisit.getCreatedAt()
        );
    }
}