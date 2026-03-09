package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;
import jakarta.validation.constraints.*;

public record CreateDoctorRequest(
        @NotBlank(message = "Doctor name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Specialization is required")
        Specialization specialization,

        @NotBlank(message = "License number is required")
        @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "License must be 2 letters followed by 6 digits (e.g. MC123456)")
        String licenseNumber,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "Invalid phone number format")
        String phone,

        @Email(message = "Invalid email format")
        String email
) {}