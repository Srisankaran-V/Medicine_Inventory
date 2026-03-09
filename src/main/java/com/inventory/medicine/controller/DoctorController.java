package com.inventory.medicine.controller;

import com.inventory.medicine.dto.doctor.CreateDoctorRequest;
import com.inventory.medicine.dto.doctor.DoctorResponse;
import com.inventory.medicine.dto.doctor.DoctorSearchCriteria;
import com.inventory.medicine.dto.doctor.UpdateDoctorRequest;
import com.inventory.medicine.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        return new ResponseEntity<>(doctorService.createDoctor(request), HttpStatus.CREATED);
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

    @GetMapping("/search")
    public ResponseEntity<Page<DoctorResponse>> searchDoctors(
            DoctorSearchCriteria criteria,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(doctorService.searchDoctors(
                criteria.keyword(),
                criteria.specialization(),
                criteria.active(),
                pageable));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DoctorResponse> deactivateDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.deactivateDoctor(id));
    }
}