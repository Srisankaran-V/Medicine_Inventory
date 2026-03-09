package com.inventory.medicine.model.billing;

import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor // Required by JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required by @Builder
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String billNumber; // e.g., INV-20260224-001

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private BigDecimal consultationFee;
    private BigDecimal medicineTotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    @Column(name = "payment_method")
    private String paymentMethod; // e.g., "CASH", "CARD", "INSURANCE"

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BillItem> items = new ArrayList<>();

    private Instant createdAt;

    @PrePersist
    protected void setInitialValue() {
        this.createdAt = Instant.now();
        if (this.status == null) this.status = BillStatus.PENDING;
    }

    /**
     * Correct Helper Method for Bi-directional mapping.
     * Ensures both the Java list and the DB Foreign Key are updated.
     */
    public void addBillItem(BillItem item) {
        if (item == null) return;
        this.items.add(item);
        item.setBill(this);
    }
}