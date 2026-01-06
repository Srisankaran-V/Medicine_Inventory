package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.Drug;
import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;

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
    public static DrugResponse toConvertDrugResponseDTO(Drug drug){
        return new DrugResponse(
                drug.getId(),
                drug.getName(),
                drug.getGenericName(),
                drug.getDrugCode(),
                drug.getDrugClassification(),
                drug.getDrugForm(),
                drug.getQuantityInStock(),
                drug.getMinStockLevel(),
                drug.getExpiryDate(),
                drug.getSellingPrice(),
                drug.getDrugStatus(),
                drug.getCreatedAt(),
                drug.getUpdatedAt()
        );
    }
}
