package com.inventory.medicine.dto.appointment;

import com.inventory.medicine.model.appointment.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        LocalDateTime appointmentDate,
        AppointmentStatus status,
        String reasonForVisit,
        LocalDateTime createdAt
) {}