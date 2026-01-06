package com.inventory.medicine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "drug_sale_item")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrugSaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drugSaleItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_sale_id", nullable = false)
    private DrugSale drugSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;


}
