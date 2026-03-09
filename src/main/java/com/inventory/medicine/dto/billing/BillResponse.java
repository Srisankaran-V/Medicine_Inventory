package com.inventory.medicine.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private Long id;
    private String billNumber;
    private Long appointmentId;
    private String patientName;
    private String patientContact;

    // Financial Breakdown
    private BigDecimal consultationFee;
    private BigDecimal medicineTotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    private String status; // PENDING, PAID, etc.
    private List<BillItemResponse> items;
    private Instant createdAt;
}