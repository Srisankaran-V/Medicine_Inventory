package com.inventory.medicine.dto.report;

public record LowStockReportDTO(
        Long drugId,
        String drugName,
        Integer quantityInStock,
        Integer minStockLevel
) {
}
