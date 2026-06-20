package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.AdminLoginRequest;
import com.vaidyo.vaidyo_backend.dto.AdminLoginResponse;
import com.vaidyo.vaidyo_backend.dto.AdminStatsResponse;
import com.vaidyo.vaidyo_backend.dto.DoctorVerificationResponse;
import com.vaidyo.vaidyo_backend.entity.Admin;
import com.vaidyo.vaidyo_backend.entity.DoctorProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.AdminRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import com.vaidyo.vaidyo_backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminService(AdminRepository adminRepository,
                        UserRepository userRepository,
                        DoctorProfileRepository doctorProfileRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ── LOGIN ──────────────────────────────────────────────────
    public AdminLoginResponse login(AdminLoginRequest request) {

        // 1. Find admin by username
        Admin admin = adminRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        // 2. Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                admin.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 3. Check admin is active
        if (admin.getStatus() != Admin.AdminStatus.ACTIVE) {
            throw new RuntimeException("Admin account is inactive");
        }

        // 4. Generate JWT with ADMIN role
        String token = jwtUtil.generateToken(
                buildAdminUserDetails(admin),
                User.Role.ADMIN.name(),
                admin.getId()
        );

        // 5. Build response
        AdminLoginResponse response = new AdminLoginResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setAdminId(admin.getId());
        response.setUsername(admin.getUsername());
        response.setFullName(admin.getFullName());
        response.setEmail(admin.getEmail());
        response.setMessage("Admin login successful");
        return response;
    }

    // ── STATS ──────────────────────────────────────────────────
    public AdminStatsResponse getStats() {

        long totalPatients = userRepository
                .countByRole(User.Role.PATIENT);
        long totalDoctors = userRepository
                .countByRole(User.Role.DOCTOR);
        long totalCaretakers = userRepository
                .countByRole(User.Role.CARETAKER);
        long pendingDoctors = doctorProfileRepository
                .countByVerificationStatus(
                        DoctorProfile.VerificationStatus.PENDING);
        long approvedDoctors = doctorProfileRepository
                .countByVerificationStatus(
                        DoctorProfile.VerificationStatus.VERIFIED);
        long rejectedDoctors = doctorProfileRepository
                .countByVerificationStatus(
                        DoctorProfile.VerificationStatus.REJECTED);

        return new AdminStatsResponse(
                totalPatients,
                totalDoctors,
                totalCaretakers,
                pendingDoctors,
                approvedDoctors,
                rejectedDoctors
        );
    }

    // ── PENDING DOCTORS ────────────────────────────────────────
    public List<DoctorVerificationResponse> getPendingDoctors() {
        return doctorProfileRepository
                .findByVerificationStatus(
                        DoctorProfile.VerificationStatus.PENDING)
                .stream()
                .map(DoctorVerificationResponse::from)
                .collect(Collectors.toList());
    }

    // ── ALL DOCTORS ────────────────────────────────────────────
    public List<DoctorVerificationResponse> getAllDoctors() {
        return doctorProfileRepository
                .findAll()
                .stream()
                .map(DoctorVerificationResponse::from)
                .collect(Collectors.toList());
    }

    // ── APPROVE DOCTOR ─────────────────────────────────────────
    public DoctorVerificationResponse approveDoctor(Long doctorProfileId) {
        DoctorProfile profile = doctorProfileRepository
                .findById(doctorProfileId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found"));

        profile.setVerificationStatus(
                DoctorProfile.VerificationStatus.VERIFIED);
        profile.setRejectionReason(null);
        doctorProfileRepository.save(profile);

        return DoctorVerificationResponse.from(profile);
    }

    // ── REJECT DOCTOR ──────────────────────────────────────────
    public DoctorVerificationResponse rejectDoctor(
            Long doctorProfileId, String reason) {

        DoctorProfile profile = doctorProfileRepository
                .findById(doctorProfileId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found"));

        profile.setVerificationStatus(
                DoctorProfile.VerificationStatus.REJECTED);
        profile.setRejectionReason(reason);
        doctorProfileRepository.save(profile);

        return DoctorVerificationResponse.from(profile);
    }

    // ── SEED DEFAULT ADMIN ─────────────────────────────────────
    // Called once on startup — creates default admin if none exists
    public void seedDefaultAdmin() {
        if (!adminRepository.existsByUsername("admin")) {
            Admin admin = new Admin(
                    "admin",
                    passwordEncoder.encode("Vaidyo@Admin123"),
                    "Super Admin",
                    "admin@vaidyo.com"
            );
            adminRepository.save(admin);
            System.out.println("✅ Default admin created — " +
                    "username: admin, password: Vaidyo@Admin123");
        }
    }

    // ── Helper: build UserDetails for JWT ─────────────────────
    private org.springframework.security.core.userdetails.UserDetails
    buildAdminUserDetails(Admin admin) {
        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getUsername())
                .password(admin.getPasswordHash())
                .roles("ADMIN")
                .build();
    }
}