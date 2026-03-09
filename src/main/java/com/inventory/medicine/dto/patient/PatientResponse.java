package com.inventory.medicine.dto.patient;

import com.inventory.medicine.model.patient.Gender;

import java.time.LocalDateTime;

public record PatientResponse(
        String name,
        Integer age,
        Gender gender,
        String phone,
        String email,
        String patientCode,
        String address,
        String city,
        String bloodGroup,
        String allergies,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
