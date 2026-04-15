package com.inventory.medicine.model.doctor;

import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.patient.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctor_license", columnList = "license_number")
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    @Builder.Default
    private Boolean active = true;

    private LocalTime shiftStart; // e.g., 09:00
    private LocalTime shiftEnd;   // e.g., 17:00
    private Integer slotDuration; // e.g., 30 (minutes)

    /* ——— Audit Fields ——— */
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void setInitialValues() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void setUpdatedValues() {
        updatedAt = LocalDateTime.now();
    }
}