package com.inventory.medicine.repository;

import com.inventory.medicine.dto.report.LowStockReportDTO;
import com.inventory.medicine.model.Drug;
import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Drug d where d.id = :id")
    Optional<Drug> findByIdForUpdate(Long id);

    @Query("""
        SELECT new com.inventory.medicine.dto.report.LowStockReportDTO(
            d.id,
            d.name,
            d.quantityInStock,
            d.minStockLevel
        )
        FROM Drug d
        WHERE d.quantityInStock <= d.minStockLevel
        ORDER BY d.quantityInStock ASC
    """)
    List<LowStockReportDTO> getLowStockDrugs();


//    Optional<Drug> findByDrugCode(String drugCode);
//    List<Drug> findByNameContainingIgnoreCase(String name);
//    List<Drug> findByGenericNameContainingIgnoreCase(String genericName);
//    List<Drug> findByDrugClassification(DrugClassification drugClassification);
//    List<Drug> findByDrugForm(DrugForm drugForm);
//
//    List<Drug> findByQuantityInStock(Integer quantityInStock);
//
//    //Stocks that are greater than
//    List<Drug> findByQuantityInStockGreaterThanEqual(Integer quantityInStock);
//
//    //Stocks that are less than
//    List<Drug> findByQuantityInStockLessThanEqual(Integer quantityInStock);
//
//    //Stocks based one Status
//    List<Drug> findByDrugStatus(DrugStatus drugStatus);
//
//    List<Drug> findByDrugStatusAndQuantityInStockGreaterThanEqual(DrugStatus drugStatus, Integer quantityInStock);
//    List<Drug> findByDrugStatusAndQuantityInStockLessThanEqual(DrugStatus drugStatus, Integer quantityInStock);
//
//    @Query("""
//            select d from Drug d where d.quantityInStock <= d.minStockLevel""")
//    List<Drug> findLowQuantityInStock();
//
//    @Query("""
//            select d from Drug d
//            where d.drugStatus = :drugStatus
//            and d.quantityInStock <= d.minStockLevel""")
//    List<Drug> findLowQuantityInStockByDrugStatus(DrugStatus drugStatus);
//
//    // Drugs already expired
//    List<Drug> findByExpiryDateBefore(LocalDateTime date);
//
//    // Drugs expiring between two dates (e.g. next 30 days)
//    List<Drug> findByExpiryDateBetween(LocalDateTime from, LocalDateTime to);
//
//
//    // ---------- CREATED / UPDATED TIMESTAMP FILTERS ----------
//
//    // Drugs created in a given time range
//    List<Drug> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
//
//    // Drugs updated in a given time range
//    List<Drug> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
//

}
