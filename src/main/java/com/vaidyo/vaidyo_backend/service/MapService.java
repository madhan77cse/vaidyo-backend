package com.vaidyo.vaidyo_backend.service;

import com.vaidyo.vaidyo_backend.dto.DoctorLocationRequest;
import com.vaidyo.vaidyo_backend.dto.NearbyAmbulancePartnerResponse;
import com.vaidyo.vaidyo_backend.dto.NearbyDoctorResponse;
import com.vaidyo.vaidyo_backend.entity.AmbulancePartner;
import com.vaidyo.vaidyo_backend.entity.DoctorLocation;
import com.vaidyo.vaidyo_backend.entity.DoctorProfile;
import com.vaidyo.vaidyo_backend.entity.User;
import com.vaidyo.vaidyo_backend.repository.AmbulancePartnerRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorLocationRepository;
import com.vaidyo.vaidyo_backend.repository.DoctorProfileRepository;
import com.vaidyo.vaidyo_backend.repository.UserRepository;
import com.vaidyo.vaidyo_backend.util.GeoUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final DoctorLocationRepository doctorLocationRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AmbulancePartnerRepository ambulancePartnerRepository;
    private final UserRepository userRepository;

    public MapService(DoctorLocationRepository doctorLocationRepository,
                      DoctorProfileRepository doctorProfileRepository,
                      AmbulancePartnerRepository ambulancePartnerRepository,
                      UserRepository userRepository) {
        this.doctorLocationRepository = doctorLocationRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.ambulancePartnerRepository = ambulancePartnerRepository;
        this.userRepository = userRepository;
    }

    // ── Doctor sets their own location ───────────────────────────

    @Transactional
    public void setMyLocation(Authentication authentication, DoctorLocationRequest request) {
        String mobileNumber = authentication.getName();
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Doctor profile not found. Complete your doctor profile setup first."));

        DoctorLocation location = doctorLocationRepository.findByDoctorProfile_Id(doctorProfile.getId())
                .orElseGet(DoctorLocation::new);

        location.setDoctorProfile(doctorProfile);
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setLocationLabel(request.getLocationLabel());

        doctorLocationRepository.save(location);
    }

    // ── Nearby doctors ───────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<NearbyDoctorResponse> findNearbyDoctors(Double latitude, Double longitude,
                                                        Double radiusKm, String speciality) {
        List<DoctorLocation> allLocations = doctorLocationRepository.findAll();

        return allLocations.stream()
                .filter(loc -> speciality == null || speciality.isBlank()
                        || loc.getDoctorProfile().getSpeciality().name().equalsIgnoreCase(speciality))
                .map(loc -> {
                    Double distance = (latitude != null && longitude != null)
                            ? GeoUtils.distanceKm(latitude, longitude, loc.getLatitude(), loc.getLongitude())
                            : null;
                    DoctorProfile dp = loc.getDoctorProfile();
                    return new NearbyDoctorResponse(
                            dp.getId(),
                            dp.getUser().getFullName(),
                            dp.getSpeciality().name(),
                            dp.getConsultationFee(),
                            dp.getClinicAddress(),
                            loc.getLatitude(),
                            loc.getLongitude(),
                            loc.getLocationLabel(),
                            distance
                    );
                })
                .filter(r -> radiusKm == null || r.getDistanceKm() == null || r.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparing(
                        NearbyDoctorResponse::getDistanceKm,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    // ── Nearby ambulance partners ─────────────────────────────────

    @Transactional(readOnly = true)
    public List<NearbyAmbulancePartnerResponse> findNearbyAmbulancePartners(Double latitude, Double longitude,
                                                                            Double radiusKm) {
        List<AmbulancePartner> partners = ambulancePartnerRepository.findAll();

        return partners.stream()
                .filter(p -> p.getBaseLatitude() != null && p.getBaseLongitude() != null)
                .map(p -> {
                    Double distance = (latitude != null && longitude != null)
                            ? GeoUtils.distanceKm(latitude, longitude, p.getBaseLatitude(), p.getBaseLongitude())
                            : null;
                    return new NearbyAmbulancePartnerResponse(
                            p.getId(), p.getPartnerName(), p.getPhoneNumber(), p.getVehicleNumber(),
                            p.getBaseLatitude(), p.getBaseLongitude(), distance
                    );
                })
                .filter(r -> radiusKm == null || r.getDistanceKm() == null || r.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparing(
                        NearbyAmbulancePartnerResponse::getDistanceKm,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}