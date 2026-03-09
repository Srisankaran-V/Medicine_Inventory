package com.inventory.medicine.controller;

import com.inventory.medicine.dto.prescription.CreatePrescriptionRequest;
import com.inventory.medicine.dto.prescription.PrescriptionResponse;
import com.inventory.medicine.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * CLINICAL ACTION: Issue a new prescription for a patient.
     * This will also automatically update the appointment status COMPLETED.
     */
    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody CreatePrescriptionRequest request) {

        return new ResponseEntity<>(prescriptionService.createPrescription(request), HttpStatus.CREATED);
    }

    /**
     * MEDICAL HISTORY: Search through past prescriptions with dynamic filters.
     * GET /api/v1/prescriptions?patientId=5&diagnosis=Fever
     */
    @GetMapping
    public ResponseEntity<Page<PrescriptionResponse>> search(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String diagnosis,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(prescriptionService.search(
                patientId, doctorId, diagnosis, fromDate, toDate, pageable));
    }

    /**
     * RETRIEVAL: Get full details of a specific prescription, including all medications.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }
}