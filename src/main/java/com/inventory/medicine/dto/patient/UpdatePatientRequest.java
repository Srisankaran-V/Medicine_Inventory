package com.inventory.medicine.dto.patient;

import com.inventory.medicine.model.patient.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePatientRequest(
        @Size(min = 2, max = 100)
        String name,
        Gender gender,

        @Min(value = 0)
        @Max(value = 150)
        Integer age,
        @Pattern(regexp = "^\\+?[0-9.]{10,15}$")
        String phone,
        String email,
        String address,
        @Pattern(regexp = "^(A|B|AB|O)[+-]$")
        String bloodGroup,
        String allergies,
        String notes
) { }

