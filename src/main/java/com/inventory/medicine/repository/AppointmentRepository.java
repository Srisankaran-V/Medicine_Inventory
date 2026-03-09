package com.inventory.medicine.repository;

import com.inventory.medicine.model.appointment.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.status NOT IN (com.inventory.medicine.model.appointment.AppointmentStatus.CANCELLED, " +
            "com.inventory.medicine.model.appointment.AppointmentStatus.NO_SHOW) " +
            "AND a.startTime < :newEndTime " +
            "AND a.startTime > :bufferStart")
    boolean hasOverlappingAppointment(
            @Param("doctorId") Long doctorId,
            @Param("newStartTime") Instant newStartTime,
            @Param("newEndTime") Instant newEndTime,
            @Param("bufferStart") Instant bufferStart
    );

    List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, Instant start, Instant end);

//    <T> Range<T> findAll(Specification<Appointment> spec, Pageable pageable);
}