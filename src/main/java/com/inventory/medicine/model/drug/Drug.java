package com.inventory.medicine.model.drug;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @NotBlank
    private String name;

    @Column(name = "generic_name")
    @NotBlank
    private String genericName;

    @Column(unique = true, name = "drug_code")
    private String drugCode;

    @Enumerated(EnumType.STRING)
    private DrugClassification drugClassification; // ex: painkiller, antibiotic, etc

    @Enumerated(EnumType.STRING)
    private DrugForm drugForm; // Tablet, Syrup, Injection...

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal sellingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "drug_status")
    private DrugStatus drugStatus; //Active, Inactive

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void setInitialValues(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if(drugStatus == null) { drugStatus = DrugStatus.ACTIVE;}
        if(quantityInStock == null) { quantityInStock = 0;}
        if(minStockLevel == null) { minStockLevel = 0;}


    }

    @PreUpdate
    private void setUpdateValues(){
        this.updatedAt = LocalDateTime.now();
    }


    

}
