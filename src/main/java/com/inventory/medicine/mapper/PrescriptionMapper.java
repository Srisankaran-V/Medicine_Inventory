package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.prescription.PrescriptionItemResponse;
import com.inventory.medicine.dto.prescription.PrescriptionResponse;
import com.inventory.medicine.model.prescription.Prescription;
import com.inventory.medicine.model.prescription.PrescriptionItem;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class PrescriptionMapper implements BaseMapper<Prescription, PrescriptionResponse> {

    @Override
    public PrescriptionResponse toDTO(Prescription prescription) {
        if (prescription == null) return null;

        // Map the nested items first
        List<PrescriptionItemResponse> itemDtos = prescription.getItems().stream()
                .map(this::toItemDTO)
                .toList();

        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getAppointment().getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getUser().getFullName(),
                prescription.getDoctor().getId(),
                prescription.getDoctor().getUser().getFullName(),
                prescription.getDiagnosis(),
                prescription.getPhysicalExamination(),
                prescription.getClinicalNotes(),
                itemDtos,
                prescription.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDateTime()
        );
    }

    // Helper method to map individual items
    private PrescriptionItemResponse toItemDTO(PrescriptionItem item) {
        return new PrescriptionItemResponse(
                item.getId(),
                item.getDrug().getId(),
                item.getDrug().getName(),
                item.getDosage(),
                item.getFrequency(),
                item.getDuration(),
                item.getInstructions()
        );
    }
}