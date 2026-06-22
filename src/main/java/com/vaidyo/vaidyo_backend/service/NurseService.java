
package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.NurseProfileRequest;
import com.vaidyo.vaidyo_backend.dto.NurseProfileResponse;
import com.vaidyo.vaidyo_backend.entity.NurseProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.NurseProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NurseService {

    private final NurseProfileRepository nurseProfileRepository;
    private final UserRepository userRepository;

    public NurseService(NurseProfileRepository nurseProfileRepository,
                        UserRepository userRepository) {
        this.nurseProfileRepository = nurseProfileRepository;
        this.userRepository = userRepository;
    }

    // ── Nurse submits their profile after registering with role=NURSE ──
    @Transactional
    public NurseProfileResponse submitProfile(Long userId, NurseProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.NURSE) {
            throw new RuntimeException("Only users registered as NURSE can submit a nurse profile");
        }

        if (nurseProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Nurse profile already submitted for this user");
        }

        NurseProfile profile = new NurseProfile();
        profile.setUser(user);
        profile.setNursingCouncilRegNumber(request.getNursingCouncilRegNumber());
        profile.setIdDocumentUrl(request.getIdDocumentUrl());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setConsultationFee(request.getConsultationFee());
        profile.setServiceAddress(request.getServiceAddress());
        profile.setBio(request.getBio());
        profile.setVerificationStatus(NurseProfile.VerificationStatus.PENDING);

        NurseProfile saved = nurseProfileRepository.save(profile);
        return new NurseProfileResponse(saved);
    }

    // ── Nurse views their own profile/status ──
    @Transactional
    public NurseProfileResponse getMyProfile(Long userId) {
        NurseProfile profile = nurseProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Nurse profile not found"));
        return new NurseProfileResponse(profile);
    }

    // ── Patients/Teammate-2 admin UI: list approved nurses ──
    @Transactional
    public List<NurseProfileResponse> getApprovedNurses() {
        return nurseProfileRepository
                .findByVerificationStatusOrderByCreatedAtDesc(NurseProfile.VerificationStatus.APPROVED)
                .stream()
                .map(NurseProfileResponse::new)
                .collect(Collectors.toList());
    }

    // ── Admin: list pending nurses for review ──
    @Transactional
    public List<NurseProfileResponse> getPendingNurses() {
        return nurseProfileRepository
                .findByVerificationStatusOrderByCreatedAtDesc(NurseProfile.VerificationStatus.PENDING)
                .stream()
                .map(NurseProfileResponse::new)
                .collect(Collectors.toList());
    }

    // ── Admin: approve ──
    @Transactional
    public NurseProfileResponse approveNurse(Long nurseProfileId) {
        NurseProfile profile = nurseProfileRepository.findById(nurseProfileId)
                .orElseThrow(() -> new RuntimeException("Nurse profile not found"));
        profile.setVerificationStatus(NurseProfile.VerificationStatus.APPROVED);
        profile.setRejectionReason(null);
        NurseProfile saved = nurseProfileRepository.save(profile);
        return new NurseProfileResponse(saved);
    }

    // ── Admin: reject ──
    @Transactional
    public NurseProfileResponse rejectNurse(Long nurseProfileId, String reason) {
        NurseProfile profile = nurseProfileRepository.findById(nurseProfileId)
                .orElseThrow(() -> new RuntimeException("Nurse profile not found"));
        profile.setVerificationStatus(NurseProfile.VerificationStatus.REJECTED);
        profile.setRejectionReason(reason);
        NurseProfile saved = nurseProfileRepository.save(profile);
        return new NurseProfileResponse(saved);
    }
}