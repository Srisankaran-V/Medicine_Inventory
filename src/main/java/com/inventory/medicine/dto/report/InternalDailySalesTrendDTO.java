package com.inventory.medicine.dto.report;

import java.math.BigDecimal;
import java.sql.Date;

public record InternalDailySalesTrendDTO(
        Date date,
        BigDecimal amount
) {
}
