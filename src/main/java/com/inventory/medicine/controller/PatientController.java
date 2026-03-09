package com.inventory.medicine.controller;

import com.inventory.medicine.dto.patient.CreatePatientRequest;
import com.inventory.medicine.dto.patient.PatientResponse;
import com.inventory.medicine.dto.patient.PatientSearchCriteria;
import com.inventory.medicine.dto.patient.UpdatePatientRequest;

import com.inventory.medicine.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;

    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest createPatientRequest){
        return ResponseEntity.ok(patientService.createPatient(createPatientRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody UpdatePatientRequest updatePatientRequest){
        return ResponseEntity.ok(patientService.updatePatient(id, updatePatientRequest));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientResponse> softDeletePatient(@PathVariable Long id){
        return ResponseEntity.ok(patientService.softDeletePatient(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponse> hardDeletePatient(@PathVariable Long id){
        return ResponseEntity.ok(patientService.hardDeletePatient(id));
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> searchPatient(
           PatientSearchCriteria criteria,

            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

            ){



        return ResponseEntity.ok(patientService.searchPatient(
                criteria,
                pageable
        ));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(patientService.findById(id));
    }

}
