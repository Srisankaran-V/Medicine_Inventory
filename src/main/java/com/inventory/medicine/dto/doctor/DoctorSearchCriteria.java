package com.inventory.medicine.dto.doctor;

import com.inventory.medicine.model.doctor.Specialization;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DoctorSearchCriteria(
        String keyword,
        Specialization specialization,
        Boolean active,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to
) {}