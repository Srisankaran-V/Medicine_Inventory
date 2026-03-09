package com.inventory.medicine.dto.auth;

import com.inventory.medicine.model.auth.Role;
import com.inventory.medicine.model.doctor.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull Role role,

        // Optional Profile Fields
        Specialization specialization, // Only if DOCTOR
        String licenseNumber,         // Only if DOCTOR
        String patientCode            // Only if PATIENT
) {}