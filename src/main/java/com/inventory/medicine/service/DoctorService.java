package com.inventory.medicine.service;

import com.inventory.medicine.dto.doctor.CreateDoctorRequest;
import com.inventory.medicine.dto.doctor.DoctorResponse;
import com.inventory.medicine.dto.doctor.DoctorSearchCriteria;
import com.inventory.medicine.dto.doctor.UpdateDoctorRequest;
import com.inventory.medicine.mapper.DoctorMapper;
import com.inventory.medicine.model.auth.Role;
import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.doctor.Specialization;
import com.inventory.medicine.repository.AuthRepository;
import com.inventory.medicine.repository.DoctorRepository;
import com.inventory.medicine.specification.DoctorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Create a new doctor with unique license verification.
     */
    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

//        log.info("Creating doctor profile for user: {}", user.getEmail());
        if(doctorRepository.existsByUser_FullNameAndAgeAndUser_PhoneAndUser_Email(request.name(), request.age(), request.phone(), request.email())) {
            throw new RuntimeException("Duplicate Patient record already exists.");
        }

        User user = authRepository.findByEmail(request.email())
                .orElseGet(() -> User.builder()
                        .email(request.email())
                        .fullName(request.name())
                        .phone(request.phone())
                        // 🔐 FIX: Always encode the password
                        .password(passwordEncoder.encode(request.password()))
                        .role(Role.DOCTOR)
                        .build());

        // 3. Security Check: Prevent one User from having multiple Patient Profiles
        // If the user already has a saved ID, check if they are linked to a doctor
        if(user.getId() != null && doctorRepository.findByUser(user).isPresent()){
            throw new RuntimeException("This account is already registered as a doctor.");
        }

        // 1. Role validation
        if (!user.getRole().name().equals("DOCTOR")) {
            throw new RuntimeException("Only DOCTOR users can create doctor profile");
        }


        // 3. License validation
        if (doctorRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new RuntimeException("License already exists");
        }

        // 4. Create doctor
        Doctor doctor = Doctor.builder()
                .user(user)
                .age(request.age())
                .gender(request.gender())
                .specialization(request.specialization())
                .licenseNumber(request.licenseNumber())
                .active(true)
                .build();

        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    /**
     * Partial update logic with null-safety.
     */
    @Transactional
    public DoctorResponse updateDoctor(Long id, UpdateDoctorRequest request) {
        log.info("Updating doctor ID: {}", id);

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        if (request.name() != null) doctor.getUser().setFullName(request.name());
        if (request.specialization() != null) doctor.setSpecialization(request.specialization());
        if (request.phone() != null) doctor.getUser().setPhone(request.phone());
        if (request.email() != null) doctor.getUser().setEmail(request.email());
        if (request.active() != null) doctor.setActive(request.active());
        // Note: LicenseNumber is excluded from updates to maintain identity integrity.
        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    /**
     * Soft delete (Deactivation)
     */
    @Transactional
    public DoctorResponse deactivateDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        doctor.setActive(false);
        log.warn("Doctor with ID {} has been deactivated.", id);
        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorResponse deleteDoctor(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: "+ id));
        doctorRepository.delete(doctor);
        return doctorMapper.toDTO(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(doctorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }

    /**
     * Dynamic search using Specifications and Pagination.
     */
    @Transactional(readOnly = true)
    public Page<DoctorResponse> searchDoctors(
            DoctorSearchCriteria criteria,
            Pageable pageable) {

        Specification<Doctor> spec = DoctorSpecification.search(criteria);
        return doctorRepository.findAll(spec, pageable).map(doctorMapper::toDTO);
    }
}