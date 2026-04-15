package com.inventory.medicine.service;

import com.inventory.medicine.dto.patient.CreatePatientRequest;
import com.inventory.medicine.dto.patient.PatientResponse;
import com.inventory.medicine.dto.patient.PatientSearchCriteria;
import com.inventory.medicine.dto.patient.UpdatePatientRequest;
import com.inventory.medicine.exception.PatientNotFoundException;
import com.inventory.medicine.mapper.PatientMapper;
import com.inventory.medicine.model.auth.Role;
import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.patient.Gender;
import com.inventory.medicine.model.patient.Patient;
import com.inventory.medicine.repository.AuthRepository;
import com.inventory.medicine.repository.PatientRepository;
import com.inventory.medicine.specification.PatientSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(
            PatientRepository patientRepository,
            PatientMapper patientMapper,
            AuthRepository authRepository, PasswordEncoder passwordEncoder
    ){
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PatientResponse findById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toDTO)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    // 1. Inject PasswordEncoder at the top of your class
    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        // 1. Duplicate Check (Clinical Profile)
        if(patientRepository.existsByUser_FullNameAndAgeAndUser_Phone(request.name(), request.age(), request.phone())) {
            throw new RuntimeException("Duplicate Patient record already exists.");
        }

        // 2. Identify or Create the User
        User user = authRepository.findByEmail(request.email())
                .orElseGet(() -> User.builder()
                        .email(request.email())
                        .fullName(request.name())
                        .phone(request.phone())
                        // 🔐 FIX: Always encode the password
                        .password(passwordEncoder.encode(request.password()))
                        .role(Role.PATIENT)
                        .build());

        // 3. Security Check: Prevent one User from having multiple Patient Profiles
        // If the user already has a saved ID, check if they are linked to a patient
        if(user.getId() != null && patientRepository.findByUser(user).isPresent()){
            throw new RuntimeException("This account is already registered as a patient.");
        }

        // 4. Build the Clinical Profile
        Patient p = Patient.builder()
                .user(user) // Link established
                .age(request.age())
                .gender(request.gender())
                .address(request.address())
                .bloodGroup(request.bloodGroup())
                .allergies((request.allergies() != null && !request.allergies().isBlank())
                        ? request.allergies() : "No Allergies")
                .notes(request.notes())
                .patientCode("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .active(true)
                .build();

        // 5. Save (CascadeType.ALL in Patient entity will handle saving the User automatically)
        return patientMapper.toDTO(patientRepository.save(p));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, UpdatePatientRequest request) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        // Update Identity (Nested User Object)
        if (request.name() != null) p.getUser().setFullName(request.name());
        if (request.phone() != null) p.getUser().setPhone(request.phone());


        // Update Clinical Profile
        if (request.gender() != null) p.setGender(request.gender());
        if (request.age() != null) p.setAge(request.age());
        if (request.address() != null) p.setAddress(request.address());
        if (request.bloodGroup() != null) p.setBloodGroup(request.bloodGroup());
        if (request.allergies() != null) p.setAllergies(request.allergies());
        if (request.notes() != null) p.setNotes(request.notes());

        return patientMapper.toDTO(patientRepository.save(p));
    }


    @Transactional
    public PatientResponse softDeletePatient(Long id){
        Patient p = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

        p.setActive(false);
        return patientMapper.toDTO(patientRepository.save(p));
    }
    @Transactional
    public PatientResponse hardDeletePatient(Long id){
        Patient p = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(p);
        return patientMapper.toDTO(p);
    }

    // Search operation
    public Page<PatientResponse> searchPatient(PatientSearchCriteria criteria, Pageable pageable){
        Specification<Patient> specs = PatientSpecification.search(criteria);
        return patientRepository
                .findAll(specs, pageable)
                .map(patientMapper::toDTO);
    }

}