package com.inventory.medicine.repository;

import com.inventory.medicine.dto.report.DrugWiseSalesReportDTO;
import com.inventory.medicine.model.DrugSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugSaleItemRepository extends JpaRepository<DrugSaleItem, Long> {
    List<DrugSaleItem> findByDrugSaleItemId(Long drugSaleItemId);

    @Query("""
        SELECT new com.inventory.medicine.dto.report.DrugWiseSalesReportDTO(
            d.id,
            d.name,
            SUM(dsi.quantity),
            SUM(dsi.lineTotal)
        )
        FROM DrugSaleItem dsi
        JOIN dsi.drug d
        GROUP BY d.id, d.name
        ORDER BY SUM(dsi.quantity) DESC
    """)
    List<DrugWiseSalesReportDTO> getDrugWiseSales();
}
