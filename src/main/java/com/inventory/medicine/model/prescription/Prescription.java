package com.inventory.medicine.model.prescription;

import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor // Required by JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required by Lombok @Builder
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, unique = true)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String physicalExamination;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Ensures the Builder doesn't set this to null
    private List<PrescriptionItem> items = new ArrayList<>();

    private Instant createdAt;

    @PrePersist
    protected void setInitialValues() {
        this.createdAt = Instant.now();
    }

    // Helper for bi-directional mapping
    public void addPrescriptionItem(PrescriptionItem item) {
        items.add(item);
        item.setPrescription(this);
    }
}