package com.inventory.medicine.dto.prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreatePrescriptionRequest(
        @NotNull(message = "Appointment ID is required")
        Long appointmentId,

        @NotBlank(message = "Diagnosis is required")
        String diagnosis,

        String physicalExamination,
        String clinicalNotes,

        @NotEmpty(message = "At least one medicine must be prescribed")
        @Valid // Critical: Triggers validation for each PrescriptionItemRequest
        List<PrescriptionItemRequest> items
) {}