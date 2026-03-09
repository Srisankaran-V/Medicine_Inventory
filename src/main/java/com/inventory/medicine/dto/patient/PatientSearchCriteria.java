package com.inventory.medicine.dto.patient;

import com.inventory.medicine.model.patient.Gender;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public record PatientSearchCriteria(
        String keyword,
        Gender gender,
        String bloodGroup,
        Integer minAge,
        Integer maxAge,
        Boolean active,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to
) {
}
