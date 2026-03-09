package com.inventory.medicine.dto.prescription;

import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionResponse(
        Long id,
        Long appointmentId,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String diagnosis,
        String physicalExamination,
        String clinicalNotes,
        List<PrescriptionItemResponse> items,
        LocalDateTime createdAt
) {}