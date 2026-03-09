package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;

public record DoctorSearchCriteria(
        String keyword,
        Specialization specialization,
        Boolean active
) {}