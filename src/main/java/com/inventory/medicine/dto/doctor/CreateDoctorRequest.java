package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;
import com.inventory.medicine.model.patient.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public record CreateDoctorRequest(

        @NotBlank(message = "Patient name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 to 100 characters")
        String name,

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age cannot be negative")
        Integer age,

        @NotNull(message = "Gender is required")
        @Enumerated(EnumType.STRING)
        Gender gender,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "Invalid phone number format")
        String phone,

        @Email(message = "Invalid email format")
        String email,

        String password,

        @NotNull(message = "Specialization is required")
        Specialization specialization,

        @NotBlank(message = "License number is required")
        String licenseNumber
) {}