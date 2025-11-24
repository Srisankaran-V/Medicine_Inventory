package com.inventory.medicine.repository.drug;

import com.inventory.medicine.model.drug.Drug;
import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import com.inventory.medicine.model.drug.DrugStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    Optional<Drug> findByDrugCode(String drugCode);
    List<Drug> findByNameContainingIgnoreCase(String name);
    List<Drug> findByGenericNameContainingIgnoreCase(String genericName);
    List<Drug> findByDrugClassification(DrugClassification drugClassification);
    List<Drug> findByDrugForm(DrugForm drugForm);

    List<Drug> findByQuantityInStock(Integer quantityInStock);

    //Stocks that are greater than
    List<Drug> findByQuantityInStockGreaterThanEqual(Integer quantityInStock);

    //Stocks that are less than
    List<Drug> findByQuantityInStockLessThanEqual(Integer quantityInStock);

    //Stocks based one Status
    List<Drug> findByDrugStatus(DrugStatus drugStatus);

    List<Drug> findByDrugStatusAndQuantityInStockGreaterThanEqual(DrugStatus drugStatus, Integer quantityInStock);
    List<Drug> findByDrugStatusAndQuantityInStockLessThanEqual(DrugStatus drugStatus, Integer quantityInStock);

    @Query("""
            select d from Drug d where d.quantityInStock <= d.minStockLevel""")
    List<Drug> findLowQuantityInStock();

    @Query("""
            select d from Drug d
            where d.drugStatus = :drugStatus
            and d.quantityInStock <= d.minStockLevel""")
    List<Drug> findLowQuantityInStockByDrugStatus(DrugStatus drugStatus);

    // Drugs already expired
    List<Drug> findByExpiryDateBefore(LocalDate date);

    // Drugs expiring between two dates (e.g. next 30 days)
    List<Drug> findByExpiryDateBetween(LocalDate from, LocalDate to);


    // ---------- CREATED / UPDATED TIMESTAMP FILTERS ----------

    // Drugs created in a given time range
    List<Drug> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // Drugs updated in a given time range
    List<Drug> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);


}
