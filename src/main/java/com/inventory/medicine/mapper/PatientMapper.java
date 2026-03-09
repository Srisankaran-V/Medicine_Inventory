package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.patient.PatientResponse;
import com.inventory.medicine.model.patient.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public PatientResponse toDTO(Patient patient) {
        if (patient == null) return null;

        return new PatientResponse(
                patient.getUser().getFullName(), // Fetch from User
                patient.getAge(),
                patient.getGender(),
                patient.getUser().getPhone(),    // Fetch from User
                patient.getUser().getEmail(),    // Fetch from User
                patient.getPatientCode(),
                patient.getAddress(),
                patient.getCity(),
                patient.getBloodGroup(),
                patient.getAllergies(),
                patient.getNotes(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}