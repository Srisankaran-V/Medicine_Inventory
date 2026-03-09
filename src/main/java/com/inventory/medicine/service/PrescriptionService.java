package com.inventory.medicine.service;

import com.inventory.medicine.dto.prescription.CreatePrescriptionRequest;
import com.inventory.medicine.dto.prescription.PrescriptionResponse;
import com.inventory.medicine.mapper.PrescriptionMapper;
import com.inventory.medicine.model.drug.Drug;
import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.appointment.AppointmentStatus;
import com.inventory.medicine.model.prescription.Prescription;
import com.inventory.medicine.model.prescription.PrescriptionItem;
import com.inventory.medicine.repository.AppointmentRepository;
import com.inventory.medicine.repository.DrugRepository;
import com.inventory.medicine.repository.PrescriptionRepository;
import com.inventory.medicine.specification.PrescriptionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DrugRepository drugRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final InventoryService inventorySyncService;

    @Transactional
    public PrescriptionResponse createPrescription(CreatePrescriptionRequest request) {
        // 1. Fetch & Validate Appointment
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (prescriptionRepository.existsByAppointmentId(request.appointmentId())) {
            throw new RuntimeException("A prescription has already been issued for this appointment.");
        }

        // 2. Build the Parent Prescription
        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .diagnosis(request.diagnosis())
                .physicalExamination(request.physicalExamination())
                .clinicalNotes(request.clinicalNotes())
                .build();

        // 3. Map nested items and handle Medication logic
        request.items().forEach(itemDto -> {
            Drug drug = drugRepository.findById(itemDto.drugId())
                    .orElseThrow(() -> new RuntimeException("Medication not found: ID " + itemDto.drugId()));

            PrescriptionItem item = PrescriptionItem.builder()
                    .drug(drug)
                    .dosage(itemDto.dosage())
                    .frequency(itemDto.frequency())
                    .duration(itemDto.duration())
                    .instructions(itemDto.instructions())
                    // Mapping the new field from your DTO to the Entity
                    .quantityToDispense(itemDto.quantity())
                    .build();

            // Critical: Ensures bidirectional link so items save with prescription_id
            prescription.addPrescriptionItem(item);
        });

        // 4. PERSIST: Save the prescription first to generate its ID
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // 5. AUDIT & SYNC: Pass the items and the new ID for the inventory ledger
        // If stock is insufficient, this call throws an exception and rolls back the save!
        inventorySyncService.deductPrescriptionStock(
                savedPrescription.getItems(),
                savedPrescription.getId()
        );

        // 6. UPDATE WORKFLOW: Set appointment to completed
        appointment.setStatus(AppointmentStatus.COMPLETED);

        log.info("Prescription ID {} successfully issued and inventory ledger updated.", savedPrescription.getId());

        return prescriptionMapper.toDTO(savedPrescription);
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionResponse> search(
            Long patientId, Long doctorId, String diagnosis,
            Instant from, Instant to, Pageable pageable) {

        Specification<Prescription> spec = PrescriptionSpecification.filter(
                patientId, doctorId, diagnosis, from, to);

        return prescriptionRepository.findAll(spec, pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getById(Long id) {
        return prescriptionRepository.findById(id)
                .map(prescriptionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Prescription record not found"));
    }
}