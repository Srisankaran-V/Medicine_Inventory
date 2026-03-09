package com.inventory.medicine.controller;

import com.inventory.medicine.dto.billing.BillResponse;
import com.inventory.medicine.model.billing.BillStatus;
import com.inventory.medicine.service.BillService;
//import com.inventory.medicine.service.BillingService;
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
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/generate/{appointmentId}")
    public ResponseEntity<Long> generate(@PathVariable Long appointmentId) {
        return new ResponseEntity<>(billService.generateBill(appointmentId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<BillResponse>> search(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) BillStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(billService.search(patientId, status, fromDate, toDate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getById(id));
    }
}