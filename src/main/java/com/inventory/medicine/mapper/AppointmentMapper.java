package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.appointment.AppointmentResponse;
import com.inventory.medicine.model.appointment.Appointment;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AppointmentMapper implements BaseMapper<Appointment, AppointmentResponse> {

    @Override
    public AppointmentResponse toDTO(Appointment appointment) {
        if (appointment == null) return null;

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getUser().getFullName() : "Unknown Patient",
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getUser().getFullName() : "Unknown Doctor",
                // Convert UTC Instant to LocalDateTime for the DTO response if your UI expects Local
                appointment.getStartTime().atZone(ZoneOffset.UTC).toLocalDateTime(),
                appointment.getStatus(),
                appointment.getReasonForVisit(),
                // Audit timestamp
                appointment.getCreatedAt() != null ?
                        appointment.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDateTime() : null
        );
    }
}