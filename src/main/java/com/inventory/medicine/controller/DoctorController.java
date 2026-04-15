package com.inventory.medicine.controller;

import com.inventory.medicine.dto.doctor.CreateDoctorRequest;
import com.inventory.medicine.dto.doctor.DoctorResponse;
import com.inventory.medicine.dto.doctor.DoctorSearchCriteria;
import com.inventory.medicine.dto.doctor.UpdateDoctorRequest;
import com.inventory.medicine.dto.patient.PatientSearchCriteria;
import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<?> createDoctor(

            @RequestBody CreateDoctorRequest request
    ) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> searchDoctors(
            DoctorSearchCriteria criteria,

            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        System.out.println("keyword : "+criteria.keyword());
        return ResponseEntity.ok(doctorService.searchDoctors(
                criteria,
                pageable));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DoctorResponse> deactivateDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.deactivateDoctor(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorResponse> deleteDoctor(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }
}