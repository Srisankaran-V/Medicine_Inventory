package com.inventory.medicine.service;

import com.inventory.medicine.dto.report.DailySalesTrendDTO;
import com.inventory.medicine.dto.report.DrugWiseSalesReportDTO;
import com.inventory.medicine.dto.report.LowStockReportDTO;
import com.inventory.medicine.dto.report.SalesSummaryDTO;
import com.inventory.medicine.repository.DrugRepository;
import com.inventory.medicine.repository.DrugSaleItemRepository;
import com.inventory.medicine.repository.DrugSaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReportService {

    private final DrugSaleRepository drugSaleRepository;
    private final DrugSaleItemRepository drugSaleItemRepository;
    private final DrugRepository drugRepository;

    public ReportService(
            DrugSaleRepository drugSaleRepository,
            DrugSaleItemRepository drugSaleItemRepository,
            DrugRepository drugRepository){
        this.drugSaleRepository = drugSaleRepository;
        this.drugSaleItemRepository = drugSaleItemRepository;
        this.drugRepository = drugRepository;
    }

    public SalesSummaryDTO saleSummaryReport(LocalDateTime startDate, LocalDateTime endDate){
        return drugSaleRepository.getSalesSummary(startDate, endDate);
    }

    public List<DailySalesTrendDTO> dailySalesTrendReport(LocalDateTime startDate, LocalDateTime endDate){
        return drugSaleRepository.getDailySalesTrend(startDate, endDate)
                .stream()
                .map(r->new DailySalesTrendDTO(
                        r.date().toLocalDate(),
                        r.amount()
                ))
                .toList();
    }

    public List<DrugWiseSalesReportDTO> drugWiseSalesReport(){
        return drugSaleItemRepository.getDrugWiseSales();
    }

    public List<LowStockReportDTO> lowStockReport(){
        return drugRepository.getLowStockDrugs();
    }
}
