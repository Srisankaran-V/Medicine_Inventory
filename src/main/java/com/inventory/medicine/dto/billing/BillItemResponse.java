package com.inventory.medicine.dto.billing;

import java.math.BigDecimal;

public record BillItemResponse(
        String itemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {}