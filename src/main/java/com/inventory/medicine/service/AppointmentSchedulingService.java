package com.inventory.medicine.service;

import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.appointment.AppointmentStatus;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.repository.AppointmentRepository;
import com.inventory.medicine.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentSchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Generate UTC range for database query
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = date.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        // Use a Set for O(1) lookups during slot generation
        Set<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorIdAndStartTimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .map(a -> a.getStartTime().atZone(ZoneOffset.UTC).toLocalTime())
                .collect(Collectors.toSet());

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime current = doctor.getShiftStart();

        while (current.plusMinutes(doctor.getSlotDuration()).isBefore(doctor.getShiftEnd()) ||
                current.plusMinutes(doctor.getSlotDuration()).equals(doctor.getShiftEnd())) {

            // Logic check: Not in a break and not already booked
            if (!bookedTimes.contains(current)) {
                availableSlots.add(current);
            }
            current = current.plusMinutes(doctor.getSlotDuration());
        }
        return availableSlots;
    }
}