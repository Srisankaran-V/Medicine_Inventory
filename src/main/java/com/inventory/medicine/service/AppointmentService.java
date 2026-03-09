package com.inventory.medicine.service;

import com.inventory.medicine.dto.appointment.AppointmentResponse;
import com.inventory.medicine.dto.appointment.CreateAppointmentRequest;
import com.inventory.medicine.mapper.AppointmentMapper;
import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.appointment.AppointmentStatus;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.patient.Patient;
import com.inventory.medicine.repository.AppointmentRepository;
import com.inventory.medicine.repository.DoctorRepository;
import com.inventory.medicine.repository.PatientRepository;
import com.inventory.medicine.specification.AppointmentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    /**
     * Industry Standard Booking with atomic conflict check.
     */
    @Transactional
    public AppointmentResponse bookAppointment(CreateAppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .filter(Doctor::getActive)
                .orElseThrow(() -> new RuntimeException("Doctor not found or inactive"));

        Instant start = request.appointmentDate().toInstant(ZoneOffset.UTC);
        int duration = (doctor.getSlotDuration() != null) ? doctor.getSlotDuration() : 30;
        Instant end = start.plus(Duration.ofMinutes(duration));

        // INDUSTRY LOGIC: To check for overlaps using only start times,
        // we look back by the maximum possible appointment duration (e.g., 2 hours).
        // Or more precisely, any appointment starting AFTER (start - its own duration).
        Instant bufferStart = start.minus(Duration.ofHours(2)); // Safe look-back window

        if (appointmentRepository.hasOverlappingAppointment(doctor.getId(), start, end, bufferStart)) {
            throw new RuntimeException("This slot is no longer available.");
        }
        Patient patient = patientRepository.getReferenceById(request.patientId());

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(start)
                .durationMinutes(duration)
                .status(AppointmentStatus.PENDING)
                .reasonForVisit(request.reasonForVisit())
                .build();

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }


    /**
     * State Machine Status Update.
     */
    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Professional logic: Prevent updating a CANCELLED appointment
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot update status of a cancelled appointment.");
        }

        appointment.setStatus(newStatus);
        log.info("Appointment ID {} changed status to {}", id, newStatus);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    /**
     * Dynamic Filtering using Specification.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> searchAppointments(
            Long doctorId, Long patientId, AppointmentStatus status,
            Instant from, Instant to, Pageable pageable) {

        Specification<Appointment> spec = AppointmentSpecification.search(
                doctorId, patientId, status, from, to);

        return appointmentRepository.findAll(spec, pageable).map(appointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
}