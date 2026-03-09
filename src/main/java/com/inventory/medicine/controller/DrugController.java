package com.inventory.medicine.controller;

import com.inventory.medicine.dto.drug.CreateDrugRequest;
import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.dto.drug.DrugSearchCriteria;
import com.inventory.medicine.dto.drug.UpdateDrugRequest;
import com.inventory.medicine.service.DrugService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/drugs")
public class DrugController {

    private final DrugService drugService;

    public DrugController(DrugService drugService){
        this.drugService = drugService;
    }

    @PostMapping
    public ResponseEntity<DrugResponse> createDrug(@Valid @RequestBody CreateDrugRequest createDrugRequest){
        DrugResponse drugResponse = drugService.createDrug(createDrugRequest);
        return new ResponseEntity<>(drugResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DrugResponse> updateDrug(@PathVariable Long id, @Valid @RequestBody UpdateDrugRequest updateDrugRequest){
        return ResponseEntity.ok(drugService.updateDrug(id, updateDrugRequest));
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<DrugResponse> softDeleteDrug(@PathVariable Long id){
        return ResponseEntity.ok(drugService.softDeleteDrug(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DrugResponse> hardDeleteDrug(@PathVariable Long id){
        return ResponseEntity.ok(drugService.hardDeleteDrug(id));
    }

    @GetMapping
    public ResponseEntity<Page<DrugResponse>> searchDrugs(
            DrugSearchCriteria criteria, // Automatically mapped!
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(drugService.searchDrugs(criteria, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrugById(@PathVariable Long id){
        return ResponseEntity.ok(drugService.findById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<DrugResponse>> getAllDrugs(){
        return ResponseEntity.ok(drugService.findAllDrugs());
    }


}
