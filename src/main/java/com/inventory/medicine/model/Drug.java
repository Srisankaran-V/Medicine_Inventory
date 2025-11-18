package com.inventory.medicine.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "drug",
        indexes = {
                @Index(name = "idx_drug_name", columnList = "name"),
                @Index(name = "idx_drug_code", columnList = "drug_code")
        })
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "generic_name")
    private String genericName;

    @Column(unique = true, name = "drug_code")
    private String drugCode;

    private String classification; // ex: painkiller, antibiotic, etc


    private String form; // Tablet, Syrup, Injection...

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(scale = 2, precision = 10)
    private BigDecimal sellingPrice;

    private String status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void setInitialValues(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if(status == null) { status = "ACTIVE";}
        if(quantityInStock == null) { quantityInStock = 0;}

    }

    @PreUpdate
    private void setUpdateValues(){
        this.updatedAt = LocalDateTime.now();
    }


    

}
