package com.inventory.medicine.dto.report;

import java.math.BigDecimal;

public record SalesSummaryDTO(
        BigDecimal totalSalesAmount,
        Long totalSalesCount
) {
}
