package com.inventory.medicine.dto.prescription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrescriptionItemRequest(
        @NotNull(message = "Drug ID is required")
        Long drugId,

        @NotBlank(message = "Dosage is required")
        String dosage,

        @NotBlank(message = "Frequency is required")
        String frequency,

        @NotBlank(message = "Duration is required")
        String duration,

        String instructions,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity // This maps to quantityToDispense in the entity
) {}