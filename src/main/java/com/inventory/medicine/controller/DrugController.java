package com.inventory.medicine.controller;

import com.inventory.medicine.dto.drug.CreateDrugRequest;
import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.dto.drug.UpdateDrugRequest;
import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;
import com.inventory.medicine.service.DrugService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;


@RestController
@RequestMapping("api/v1/drugs")
public class DrugController {

    private final DrugService drugService;

    public DrugController(DrugService drugService){
        this.drugService = drugService;
    }

    @PostMapping("/add")
    public ResponseEntity<DrugResponse> createDrug(@Valid @RequestBody CreateDrugRequest createDrugRequest){
        DrugResponse drugResponse = drugService.createDrug(createDrugRequest);
        return new ResponseEntity<>(drugResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DrugResponse> updateDrug(@PathVariable Long id, @Valid @RequestBody UpdateDrugRequest updateDrugRequest){
        return ResponseEntity.ok(drugService.updateDrug(id, updateDrugRequest));
    }
    @PutMapping("/{id}/soft_delete")
    public ResponseEntity<DrugResponse> softDeleteDrug(@PathVariable Long id){
        return ResponseEntity.ok(drugService.softDeleteDrug(id));
    }
    @DeleteMapping("/{id}/hard_delete")
    public ResponseEntity<DrugResponse> hardDeleteDrug(@PathVariable Long id){
        return ResponseEntity.ok(drugService.hardDeleteDrug(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DrugResponse>> searchDrugs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) DrugClassification classification,
            @RequestParam(required = false) DrugForm form,
            @RequestParam(required = false) DrugStatus status,
            @RequestParam(defaultValue = "false") boolean lowStock,

            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String quantityOperation,


            @RequestParam(required = false) String dateField,
            @RequestParam(required = false) String dateOperation,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            // ✅ pagination & sorting
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        return ResponseEntity.ok(
                drugService.searchDrugs(
                        keyword,
                        classification,
                        form,
                        status,
                        lowStock,
                        quantity,
                        quantityOperation,
                        dateField,
                        dateOperation,
                        from,
                        to,
                        page,
                        size,
                        sort
                )
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrugById(@PathVariable Long id){
        return ResponseEntity.ok(drugService.findById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<DrugResponse>> getAllDrugs(){
        return ResponseEntity.ok(drugService.findAllDrugs());
    }
//    @GetMapping("/code/{drugCode}")
//    public ResponseEntity<DrugResponse> getByDrugCode(@PathVariable String drugCode){
//        return drugService.findByDrugCode(drugCode).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//    @GetMapping("/search/name")
//    public ResponseEntity<List<DrugResponse>> getDrugsByName(@RequestParam String name){
//        return ResponseEntity.ok(drugService.findByDrugName(name));
//    }
//    @GetMapping("/search/generic_name")
//    public ResponseEntity<List<DrugResponse>> getDrugsByGenericName(@RequestParam String genericName){
//        return ResponseEntity.ok(drugService.findByGenericName(genericName));
//    }
//    @GetMapping("/filter/classification")
//    public ResponseEntity<List<DrugResponse>> getDrugsByClassification(@RequestParam DrugClassification drugClassification){
//        return ResponseEntity.ok(drugService.findByDrugClassification(drugClassification));
//    }
//    @GetMapping("/filter/form")
//    public ResponseEntity<List<DrugResponse>> getDrugsByForm(@RequestParam DrugForm drugForm){
//        return ResponseEntity.ok(drugService.findByDrugForm(drugForm));
//    }
//    @GetMapping("/filter/status")
//    public ResponseEntity<List<DrugResponse>> getDrugsByStatus(@RequestParam DrugStatus drugStatus){
//        return ResponseEntity.ok(drugService.findByDrugStatus(drugStatus));
//    }
//    @GetMapping("/filter/equals/{quantityInStock}")
//    public ResponseEntity<List<DrugResponse>> getDrugsByQuantityInStock(@PathVariable Integer quantityInStock){
//        return ResponseEntity.ok(drugService.findByQuantityInStock(quantityInStock));
//    }
//    @GetMapping("/filter/greater_than/{quantityInStock}")
//    public ResponseEntity<List<DrugResponse>> getDrugsByQuantityInStockGreaterThanEqual(@PathVariable Integer quantityInStock){
//        return ResponseEntity.ok(drugService.findByQuantityInStockGreaterThanEqual(quantityInStock));
//    }
//    @GetMapping("/filter/less_than/{quantityInStock}")
//    public ResponseEntity<List<DrugResponse>> getDrugsByQuantityInStockLessThanEqual(@PathVariable Integer quantityInStock){
//        return ResponseEntity.ok(drugService.findByQuantityInStockLessThanEqual(quantityInStock));
//    }
//    @GetMapping("/filter/greater_than/status/{quantityInStock}")
//    public ResponseEntity<List<DrugResponse>> getDrugsByDrugStatusAndQuantityInStockGreaterThanEqual(@RequestParam DrugStatus drugStatus, @PathVariable Integer quantityInStock){
//        return ResponseEntity.ok(drugService.findByDrugStatusAndQuantityInStockGreaterThanEqual(drugStatus,quantityInStock));
//    }
//    @GetMapping("/filter/less_than/status/{quantityInStock}")
//    public ResponseEntity<List<DrugResponse>> getDrugsByDrugStatusAndQuantityInStockLessThanEqual(@RequestParam DrugStatus drugStatus, @PathVariable Integer quantityInStock){
//        return ResponseEntity.ok(drugService.findByDrugStatusAndQuantityInStockLessThanEqual(drugStatus,quantityInStock));
//    }
//    @GetMapping("/filter/low_quantity")
//    public ResponseEntity<List<DrugResponse>> getDrugsByLowQuantityInStock(){
//        return ResponseEntity.ok(drugService.findLowQuantityInStock());
//    }
//    @GetMapping("/filter/low_quantity/status")
//    public ResponseEntity<List<DrugResponse>> getDrugsByLowQuantityInStockAndDrugStatus(@RequestParam DrugStatus drugStatus){
//        return ResponseEntity.ok(drugService.findLowQuantityInStockByDrugStatus(drugStatus));
//    }
//    @GetMapping("/filter/before_expiry")
//    public ResponseEntity<List<DrugResponse>> getDrugsByExpiryDateBefore(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime expiryDate){
//        return ResponseEntity.ok(drugService.findByExpiryDateBefore(expiryDate));
//    }
//    @GetMapping("/filter/between_expiry")
//    public ResponseEntity<List<DrugResponse>> getDrugsByExpiryDateBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to){
//        return ResponseEntity.ok(drugService.findByExpiryDateBetween(from, to));
//    }
//    @GetMapping("/filter/between_created")
//    public ResponseEntity<List<DrugResponse>> getDrugsByCreatedAtBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
//        return ResponseEntity.ok(drugService.findByCreatedAtBetween(from, to));
//    }
//    @GetMapping("/filter/between_update")
//    public ResponseEntity<List<DrugResponse>> getDrugsByUpdatedAtBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
//        return ResponseEntity.ok(drugService.findByUpdatedAtBetween(from, to));
//    }

}
