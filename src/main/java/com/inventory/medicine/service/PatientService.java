package com.inventory.medicine.service;

import com.inventory.medicine.dto.patient.CreatePatientRequest;
import com.inventory.medicine.dto.patient.PatientResponse;
import com.inventory.medicine.dto.patient.PatientSearchCriteria;
import com.inventory.medicine.dto.patient.UpdatePatientRequest;
import com.inventory.medicine.exception.PatientNotFoundException;
import com.inventory.medicine.mapper.PatientMapper;
import com.inventory.medicine.model.patient.Gender;
import com.inventory.medicine.model.patient.Patient;
import com.inventory.medicine.repository.PatientRepository;
import com.inventory.medicine.specification.PatientSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper){
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public PatientResponse findById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toDTO)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        // 1. Corrected Duplicate Check (Using the nested User property)
        if(patientRepository.existsByUser_FullNameAndAgeAndUser_Phone(request.name(), request.age(), request.phone())) {
            throw new RuntimeException("Duplicate Patient record already exists.");
        }

        // Note: For a real app, you should create a User entity here
        // OR call authService.registerPatient().
        // For now, I'll assume the User is handled via cascading.

        Patient p = Patient.builder()
                .age(request.age())
                .gender(request.gender())
                .address(request.address())
                .bloodGroup(request.bloodGroup())
                .allergies(request.allergies())
                .notes(request.notes())
                .patientCode("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .active(true)
                .build();

        return patientMapper.toDTO(patientRepository.save(p));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, UpdatePatientRequest request) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        // Update Identity (Nested User Object)
        if (request.name() != null) p.getUser().setFullName(request.name());
        if (request.phone() != null) p.getUser().setPhone(request.phone());
        if (request.email() != null) p.getUser().setEmail(request.email());

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