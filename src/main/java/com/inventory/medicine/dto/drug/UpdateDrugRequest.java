package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import com.inventory.medicine.model.drug.DrugStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateDrugRequest(
        String name,
        String genericName,
        String drugCode,
        DrugClassification drugClassification,
        DrugForm drugForm,
        Integer quantityInStock,
        Integer minStockLevel,
        LocalDate expiryDate,
        BigDecimal sellingPrice,
        DrugStatus drugStatus
) { }
