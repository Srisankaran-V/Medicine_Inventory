package com.inventory.medicine.dto.appointment;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull(message = "Patient ID is required")
        Long patientId,

        @NotNull(message = "Doctor ID is required")
        Long doctorId,

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment must be scheduled for a future date")
        LocalDateTime appointmentDate,

        @Size(max = 500, message = "Reason cannot exceed 500 characters")
        String reasonForVisit
) {}