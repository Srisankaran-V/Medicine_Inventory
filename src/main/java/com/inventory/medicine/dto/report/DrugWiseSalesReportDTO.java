package com.inventory.medicine.dto.report;

import java.math.BigDecimal;

public record DrugWiseSalesReportDTO(
        Long drugId,
        String drugName,
        Long totalQuantitySole,
        BigDecimal totalRevenue
) {
}
