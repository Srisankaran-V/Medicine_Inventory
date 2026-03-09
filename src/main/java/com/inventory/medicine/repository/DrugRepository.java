package com.inventory.medicine.repository;


import com.inventory.medicine.model.drug.Drug;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {
    @Query("SELECT d FROM Drug d WHERE d.quantityInStock <= d.minStockLevel AND d.drugStatus = 'ACTIVE'")
    List<Drug> findLowStockDrugs();
}
