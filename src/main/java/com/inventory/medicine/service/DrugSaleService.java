package com.inventory.medicine.service;

import com.inventory.medicine.dto.sale.DrugSaleItemRequestDTO;
import com.inventory.medicine.dto.sale.DrugSaleRequestDTO;
import com.inventory.medicine.model.Drug;
import com.inventory.medicine.model.DrugSale;
import com.inventory.medicine.model.DrugSaleItem;
import com.inventory.medicine.model.DrugStatus;
import com.inventory.medicine.repository.DrugRepository;

import com.inventory.medicine.repository.DrugSaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DrugSaleService {

    private final DrugRepository drugRepository;
    private final DrugSaleRepository drugSaleRepository;

    public DrugSaleService(DrugSaleRepository drugSaleRepository, DrugRepository drugRepository){
        this.drugRepository = drugRepository;
        this.drugSaleRepository = drugSaleRepository;
    }

    //Create Sale

    @Transactional
    public void createSale(DrugSaleRequestDTO drugSaleRequestDTO){
        DrugSale drugSale = new DrugSale();

        drugSale.setPatientId(drugSaleRequestDTO.patientId());
        drugSale.setSoldAt(LocalDateTime.now());
        List<DrugSaleItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        Set<Long> itemIdSet = new HashSet<>();

        for(DrugSaleItemRequestDTO item : drugSaleRequestDTO.drugSaleItems()){
            if(itemIdSet.contains(item.drugId()) ){
                throw new RuntimeException(item.drugId()+" Item id is duplicate");
            }
            if (item.quantity() == null || item.quantity() <= 0) {
                throw new RuntimeException("Invalid quantity");
            }

            itemIdSet.add(item.drugId());


            Drug drug = drugRepository.findByIdForUpdate(item.drugId()).orElseThrow(()->new RuntimeException("Can't find the Drug using the drug_id "+item.drugId()));

            //Active Validation
            if( drug.getDrugStatus() != DrugStatus.ACTIVE){
                throw new RuntimeException(drug.getName()+" Drug status is INACTIVE");
            }

            //Stock Validation
            if(drug.getQuantityInStock() < item.quantity()){
                throw new RuntimeException(drug.getName()+" have Insufficient stock");
            }

            //Stock Reduce Logic
            drug.setQuantityInStock(drug.getQuantityInStock() - item.quantity());

            DrugSaleItem drugSaleItem = new DrugSaleItem();
            drugSaleItem.setDrugSale(drugSale);
            drugSaleItem.setDrug(drug);
            drugSaleItem.setQuantity(item.quantity());
            drugSaleItem.setPrice(drug.getSellingPrice());

            BigDecimal lineTotal = drug.getSellingPrice().multiply(BigDecimal.valueOf(item.quantity()));

            drugSaleItem.setLineTotal(lineTotal);

            items.add(drugSaleItem);
            totalAmount = totalAmount.add(lineTotal);

        }

        drugSale.setTotalAmount(totalAmount);
        drugSale.setItems(items);

        drugSaleRepository.save(drugSale);



    }
}
