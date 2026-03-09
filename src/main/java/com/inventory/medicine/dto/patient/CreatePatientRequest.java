package com.inventory.medicine.dto.patient;

import com.inventory.medicine.model.patient.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public record CreatePatientRequest(
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

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Blood group is required")
        @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group (e.g., A+, O-)")
        String bloodGroup,
        String allergies,

        @Size(max = 1000)
        String notes
        ) { }
