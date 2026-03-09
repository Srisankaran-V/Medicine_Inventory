package com.inventory.medicine.service;

import com.inventory.medicine.model.drug.Drug;
import com.inventory.medicine.model.drug.InventoryLog;
import com.inventory.medicine.model.prescription.PrescriptionItem;
import com.inventory.medicine.repository.DrugRepository;
import com.inventory.medicine.repository.InventoryRepository; // New Import
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final DrugRepository drugRepository;
    private final InventoryRepository inventoryLogRepository; // New Injection

    /**
     * Deducts stock and creates an audit trail.
     * Use Propagation.MANDATORY to ensure this runs inside the Prescription transaction.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deductPrescriptionStock(List<PrescriptionItem> items, Long prescriptionId) {
        for (PrescriptionItem item : items) {
            // Re-fetch or use the managed entity to ensure we have the latest quantityInStock
            Drug drug = drugRepository.findById(item.getDrug().getId())
                    .orElseThrow(() -> new RuntimeException("Drug not found: " + item.getDrug().getName()));

            // Use the professional 'quantityToDispense' field we added to PrescriptionItem
            Integer quantityToDeduct = item.getQuantityToDispense();

            log.info("Deducting {} units of {} for Prescription ID: {}", quantityToDeduct, drug.getName(), prescriptionId);

            // 1. Update the Drug table
            drug.deductStock(quantityToDeduct);
            drugRepository.save(drug);

            // 2. Create the Inventory Log (The Audit Trail)
            InventoryLog logEntry = InventoryLog.builder()
                    .drug(drug)
                    .quantityChange(-quantityToDeduct) // Negative for deduction
                    .transactionType("PRESCRIPTION")
                    .referenceId("RX-" + prescriptionId)
                    .build();

            inventoryLogRepository.save(logEntry);
        }
    }

    /**
     * Professional addition for restocking drugs (useful for your Pharmacist module)
     */
    @Transactional
    public void restockDrug(Long drugId, Integer addedQty, String invoiceRef) {
        Drug drug = drugRepository.findById(drugId)
                .orElseThrow(() -> new RuntimeException("Drug not found"));

        drug.setQuantityInStock(drug.getQuantityInStock() + addedQty);
        drugRepository.save(drug);

        inventoryLogRepository.save(InventoryLog.builder()
                .drug(drug)
                .quantityChange(addedQty)
                .transactionType("RESTOCK")
                .referenceId(invoiceRef)
                .build());
    }
}