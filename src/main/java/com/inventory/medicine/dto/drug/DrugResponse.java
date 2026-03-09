package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.drug.Drug;
import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import com.inventory.medicine.model.drug.DrugStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record DrugResponse(
        Long id,
        String name,
        String genericName,
        String drugCode,
        DrugClassification drugClassification,
        DrugForm drugForm,
        Integer quantityInStock,
        Integer minStockLevel,
        LocalDateTime expiryDate,
        BigDecimal sellingPrice,
        DrugStatus drugStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
