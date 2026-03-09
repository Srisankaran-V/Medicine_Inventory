package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.billing.BillItemResponse;
import com.inventory.medicine.dto.billing.BillResponse;
import com.inventory.medicine.model.billing.Bill;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillMapper {

    public BillResponse toDTO(Bill bill) {
        if (bill == null) return null;

        // Defensive check for items list
        List<BillItemResponse> itemDtos = bill.getItems() == null ? List.of() :
                bill.getItems().stream()
                        .map(item -> new BillItemResponse(
                                item.getItemName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubTotal()
                        ))
                        .toList(); // Using .toList() instead of .collect(Collectors.toList()) for Java 16+

        return BillResponse.builder()
                .id(bill.getId())
                .billNumber(bill.getBillNumber())
                .appointmentId(bill.getAppointment().getId())
                .patientName(bill.getPatient().getUser().getFullName())
                // Verify if your field is getPhone() or getContactNumber()
                .patientContact(bill.getPatient().getUser().getPhone())
                .consultationFee(bill.getConsultationFee())
                .medicineTotal(bill.getMedicineTotal())
                .taxAmount(bill.getTaxAmount())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus() != null ? bill.getStatus().name() : null)
                .items(itemDtos)
                .createdAt(bill.getCreatedAt())
                .build();
    }
}