package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

public record UpdateDoctorRequest(
        @Size(min = 2, max = 100)
        String name,

        Specialization specialization,

        @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "Invalid phone number format")
        String phone,

        @Email(message = "Invalid email format")
        String email,
        LocalTime shiftStart,
        LocalTime shiftEnd,
        Integer slotDuration,

        Boolean active
) {}
