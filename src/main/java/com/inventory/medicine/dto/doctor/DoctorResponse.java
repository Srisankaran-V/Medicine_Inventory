package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;
import java.time.LocalDateTime;

public record DoctorResponse(
        Long id,
        String name,
        Specialization specialization,
        String licenseNumber,
        String phone,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}