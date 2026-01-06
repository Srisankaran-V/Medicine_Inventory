package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateDrugRequest(
        String name,
        String genericName,
        String drugCode,
        DrugClassification drugClassification,
        DrugForm drugForm,
        Integer quantityInStock,
        Integer minStockLevel,
        LocalDateTime expiryDate,
        BigDecimal sellingPrice,
        DrugStatus drugStatus
) { }
