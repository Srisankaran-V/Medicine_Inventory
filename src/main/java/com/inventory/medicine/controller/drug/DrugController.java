package com.inventory.medicine.controller.drug;

import com.inventory.medicine.dto.drug.CreateDrugRequest;
import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.dto.drug.UpdateDrugRequest;
import com.inventory.medicine.model.drug.DrugClassification;
import com.inventory.medicine.model.drug.DrugForm;
import com.inventory.medicine.model.drug.DrugStatus;
import com.inventory.medicine.service.drug.DrugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/drug")
public class DrugController {

    private final DrugService drugService;

    public DrugController(DrugService drugService){
        this.drugService = drugService;
    }

    @PostMapping("/add")
    public ResponseEntity<DrugResponse> createDrug(@RequestBody CreateDrugRequest createDrugRequest){
        DrugResponse drugResponse = drugService.createDrug(createDrugRequest);
        return new ResponseEntity<>(drugResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}/update")
    public ResponseEntity<DrugResponse> updateDrug(@PathVariable Long id, @RequestBody UpdateDrugRequest updateDrugRequest){
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

    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrugById(@PathVariable Long id){
        return ResponseEntity.ok(drugService.findById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<DrugResponse>> getAllDrugs(){
        return ResponseEntity.ok(drugService.findAllDrugs());
    }
    @GetMapping("/code/{drugCode}")
    public ResponseEntity<DrugResponse> getByDrugCode(@PathVariable String drugCode){
        return drugService.findByDrugCode(drugCode).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search/name")
    public ResponseEntity<List<DrugResponse>> getDrugsByName(@RequestParam String name){
        return ResponseEntity.ok(drugService.findByDrugName(name));
    }
    @GetMapping("/search/generic_name")
    public ResponseEntity<List<DrugResponse>> getDrugsByGenericName(@RequestParam String genericName){
        return ResponseEntity.ok(drugService.findByGenericName(genericName));
    }
    @GetMapping("/filter/classification")
    public ResponseEntity<List<DrugResponse>> getDrugsByClassification(@RequestParam DrugClassification drugClassification){
        return ResponseEntity.ok(drugService.findByDrugClassification(drugClassification));
    }
    @GetMapping("/filter/form")
    public ResponseEntity<List<DrugResponse>> getDrugsByForm(@RequestParam DrugForm drugForm){
        return ResponseEntity.ok(drugService.findByDrugForm(drugForm));
    }
    @GetMapping("/filter/status")
    public ResponseEntity<List<DrugResponse>> getDrugsByStatus(@RequestParam DrugStatus drugStatus){
        return ResponseEntity.ok(drugService.findByDrugStatus(drugStatus));
    }








}
