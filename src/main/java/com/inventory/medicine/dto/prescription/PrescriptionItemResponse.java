package com.inventory.medicine.dto.prescription;

public record PrescriptionItemResponse(
        Long id,
        Long drugId,
        String drugName,
        String dosage,
        String frequency,
        String duration,
        String instructions
) {}