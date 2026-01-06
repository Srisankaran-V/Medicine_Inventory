package com.inventory.medicine.dto.sale;

import com.inventory.medicine.model.DrugSaleItem;
import com.inventory.medicine.repository.DrugSaleItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DrugSaleRequestDTO(
        @NotNull(message = "patient_id is required")
        Long patientId,
        @NotEmpty(message = "sale required at least one item")
        @Valid
        List<DrugSaleItemRequestDTO> drugSaleItems
) {
}
