package com.inventory.medicine.dto.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DrugSaleItemRequestDTO(

    @NotNull(message = "Drug id is required")
    Long drugId,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity

){}
