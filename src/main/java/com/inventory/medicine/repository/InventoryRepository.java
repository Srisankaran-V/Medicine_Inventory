package com.inventory.medicine.repository;

import com.inventory.medicine.model.drug.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryLog, Long> {
    // Returns the most recent movements first
    List<InventoryLog> findByDrugIdOrderByCreatedAtDesc(Long drugId);
}