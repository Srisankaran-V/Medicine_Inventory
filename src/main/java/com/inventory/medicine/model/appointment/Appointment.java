package com.inventory.medicine.model.appointment;

import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appt_time", columnList = "start_time"),
        @Index(name = "idx_appt_doctor_status", columnList = "doctor_id, status")
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Integer durationMinutes; // Standardizes time blocks (e.g., 15, 30, 60)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String reasonForVisit;

    @Version
    private Integer version; // Prevents double-booking via race conditions

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    protected void setInitialValues() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.status == null) this.status = AppointmentStatus.PENDING;
    }

    @PreUpdate
    protected void setUpdatedValues() {
        this.updatedAt = Instant.now();
    }
}