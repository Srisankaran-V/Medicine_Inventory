package com.inventory.medicine.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;


public record DailySalesTrendDTO(
        LocalDate date,
        BigDecimal amount
) {
}
