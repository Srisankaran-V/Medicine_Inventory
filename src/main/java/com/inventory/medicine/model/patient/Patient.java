package com.inventory.medicine.model.patient;

import com.inventory.medicine.model.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patient_code", columnList = "patientCode")
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /* ================= BASIC INFO ================= */
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    @Column(unique = true, nullable = false)
    private String patientCode;

    /* ================= ADDRESS ================= */
    private String address;
    private String city;

    /* ================= MEDICAL ================= */
    private String bloodGroup;
    private String allergies;

    @Column(length = 1000)
    private String notes;

    /* ================= STATUS ================= */
    @Builder.Default
    private Boolean active = true;

    /* ================= AUDIT ================= */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* ================= AUTO TIMESTAMP ================= */
    @PrePersist
    public void setInitialValues() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedValues() {
        updatedAt = LocalDateTime.now();
    }
}
