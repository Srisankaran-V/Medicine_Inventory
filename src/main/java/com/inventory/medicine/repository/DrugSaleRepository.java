package com.inventory.medicine.repository;

import com.inventory.medicine.dto.report.DailySalesTrendDTO;
import com.inventory.medicine.dto.report.InternalDailySalesTrendDTO;
import com.inventory.medicine.dto.report.SalesSummaryDTO;
import com.inventory.medicine.model.DrugSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DrugSaleRepository extends JpaRepository<DrugSale, Long> {

    @Query("""
        SELECT new com.inventory.medicine.dto.report.SalesSummaryDTO(
            COALESCE(SUM(ds.totalAmount), 0),
            COUNT(ds)
        )
        FROM DrugSale ds
        WHERE ds.soldAt BETWEEN :startDate AND :endDate
    """)
    SalesSummaryDTO getSalesSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT new com.inventory.medicine.dto.report.InternalDailySalesTrendDTO(
            CAST(FUNCTION('date', ds.soldAt) AS date),
            SUM(ds.totalAmount)
        )
        FROM DrugSale ds
        WHERE ds.soldAt BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('date', ds.soldAt)
        ORDER BY FUNCTION('date', ds.soldAt)
    """)
    List<InternalDailySalesTrendDTO> getDailySalesTrend(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
