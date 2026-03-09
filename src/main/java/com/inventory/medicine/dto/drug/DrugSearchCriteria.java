package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import com.inventory.medicine.model.drug.DrugStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DrugSearchCriteria(
        String keyword,
        DrugClassification classification,
        DrugForm form,
        DrugStatus status,
        boolean lowStock,
        Integer quantity,
        String quantityOperation,
        String dateField,
        String dateOperation,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
) {}