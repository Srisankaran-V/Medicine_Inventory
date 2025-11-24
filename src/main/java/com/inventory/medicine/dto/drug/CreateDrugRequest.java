package com.inventory.medicine.dto.drug;

import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateDrugRequest(
        @NotBlank String name,
        String genericName,
        String drugCode,
        @NotNull DrugClassification drugClassification,
        @NotNull DrugForm drugForm,
        @NotNull @Min(0) Integer quantityInStock,
        @NotNull @Min(0) Integer minStockLevel,
        @NotNull LocalDate expiryDate,
        @NotNull @DecimalMin("0.0") BigDecimal sellingPrice
) { }
