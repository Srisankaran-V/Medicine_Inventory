package com.inventory.medicine.service;

import com.inventory.medicine.dto.billing.BillResponse;
import com.inventory.medicine.mapper.BillMapper;
import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.billing.Bill;
import com.inventory.medicine.model.billing.BillItem;
import com.inventory.medicine.model.billing.BillStatus;
import com.inventory.medicine.model.prescription.Prescription;
import com.inventory.medicine.repository.AppointmentRepository;
import com.inventory.medicine.repository.BillRepository;
import com.inventory.medicine.repository.PrescriptionRepository;
import com.inventory.medicine.specification.BillSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final BillMapper billMapper;

    @Transactional
    public Long generateBill(Long appointmentId) {
        if (billRepository.existsByAppointmentId(appointmentId)) {
            throw new RuntimeException("Invoice already exists for this appointment.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Prescription prescription = prescriptionRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Fixed Fee logic - can be moved to Doctor entity later
        BigDecimal consultationFee = BigDecimal.valueOf(50.00);

        Bill bill = Bill.builder()
                .billNumber(generateBillNumber())
                .appointment(appointment)
                .patient(appointment.getPatient())
                .consultationFee(consultationFee)
                .status(BillStatus.PENDING)
                .build();

        BigDecimal medicineTotal = BigDecimal.ZERO;

        for (var pItem : prescription.getItems()) {
            BigDecimal price = pItem.getDrug().getPrice();
            int qty = 1; // Defaulting to 1 for MVP
            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(qty));

            bill.addBillItem(BillItem.builder()
                    .itemName(pItem.getDrug().getName())
                    .quantity(qty)
                    .unitPrice(price)
                    .subTotal(subTotal)
                    .build());

            medicineTotal = medicineTotal.add(subTotal);
        }

        BigDecimal taxRate = BigDecimal.valueOf(0.08); // 8% Tax
        BigDecimal preTaxTotal = consultationFee.add(medicineTotal);
        BigDecimal taxAmount = preTaxTotal.multiply(taxRate);

        bill.setMedicineTotal(medicineTotal);
        bill.setTaxAmount(taxAmount);
        bill.setTotalAmount(preTaxTotal.add(taxAmount));

        return billRepository.save(bill).getId();
    }

    @Transactional(readOnly = true)
    public Page<BillResponse> search(Long patientId, BillStatus status, Instant start, Instant end, Pageable pageable) {
        Specification<Bill> spec = BillSpecification.filter(patientId, status, start, end);
        return billRepository.findAll(spec, pageable).map(billMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public BillResponse getById(Long id) {
        return billRepository.findById(id)
                .map(billMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    private String generateBillNumber() {
        return "INV-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}