package com.inventory.medicine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drug_sale")
public class DrugSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drug_sale_id")
    private Long drugSaleId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "total_amount", nullable = false,precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "sold_at", nullable = false)
    private LocalDateTime soldAt;

    @OneToMany(
            mappedBy = "drugSale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DrugSaleItem> items;


}
