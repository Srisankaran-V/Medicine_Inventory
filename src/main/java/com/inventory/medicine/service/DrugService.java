package com.inventory.medicine.service;

import com.inventory.medicine.dto.drug.CreateDrugRequest;
import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.dto.drug.DrugSearchCriteria;
import com.inventory.medicine.dto.drug.UpdateDrugRequest;
import com.inventory.medicine.mapper.DrugMapper;
import com.inventory.medicine.model.drug.Drug;
import com.inventory.medicine.model.drug.DrugStatus;
import com.inventory.medicine.repository.DrugRepository;
import com.inventory.medicine.specification.DrugSpecification;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final DrugMapper drugMapper;

    public DrugService(DrugRepository drugRepository, DrugMapper drugMapper){
        this.drugRepository = drugRepository;
        this.drugMapper = drugMapper;
    }

    //Mappers-------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public DrugResponse findById(Long id) {
        return drugRepository.findById(id)
                .map(drugMapper::toDTO) // Clean and readable
                .orElseThrow(() -> new RuntimeException("Drug not found"));
    }
    @Transactional(readOnly = true)
    public List<DrugResponse> findAllDrugs(){
        return drugMapper.toDTOList(drugRepository.findAll());
    }
    //CRUD operations---------------------------------------------------------------------

    //Create new drug
    @Transactional
    public DrugResponse createDrug(CreateDrugRequest createDrugRequest){
        Drug drug = new Drug();
        drug.setName(createDrugRequest.name());
        drug.setGenericName(createDrugRequest.genericName());
        drug.setDrugCode(createDrugRequest.drugCode());
        drug.setDrugClassification(createDrugRequest.drugClassification());
        drug.setDrugForm(createDrugRequest.drugForm());
        drug.setQuantityInStock(createDrugRequest.quantityInStock());
        drug.setMinStockLevel(createDrugRequest.minStockLevel());
        drug.setExpiryDate(createDrugRequest.expiryDate());
        drug.setPrice(createDrugRequest.sellingPrice());
        drug.setDrugStatus(DrugStatus.ACTIVE);

        Drug createdDrug = drugRepository.save(drug);
        System.out.println(createdDrug);

        return drugMapper.toDTO(createdDrug);

    }




    @Transactional
    public DrugResponse updateDrug(Long id, UpdateDrugRequest updateDrugRequest){
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drug not found"));

        if(updateDrugRequest.name() != null){drug.setName(updateDrugRequest.name());}
        if(updateDrugRequest.genericName() != null){drug.setGenericName(updateDrugRequest.genericName());}
        if(updateDrugRequest.drugCode() != null){drug.setDrugCode(updateDrugRequest.drugCode());}
        if(updateDrugRequest.drugClassification() != null){drug.setDrugClassification(updateDrugRequest.drugClassification());}
        if(updateDrugRequest.drugForm() != null){drug.setDrugForm(updateDrugRequest.drugForm());}
        if(updateDrugRequest.drugStatus() != null){drug.setDrugStatus(updateDrugRequest.drugStatus());}
        if(updateDrugRequest.quantityInStock() != null){drug.setQuantityInStock(updateDrugRequest.quantityInStock());}
        if(updateDrugRequest.minStockLevel() != null){drug.setMinStockLevel(updateDrugRequest.minStockLevel());}
        if(updateDrugRequest.expiryDate() != null){drug.setExpiryDate(updateDrugRequest.expiryDate());}
        if(updateDrugRequest.sellingPrice() != null){drug.setPrice(updateDrugRequest.sellingPrice());}

        Drug updatedDrug = drugRepository.save(drug);
        return drugMapper.toDTO(updatedDrug);


    }

    @Transactional
    public DrugResponse softDeleteDrug(Long id){
        Drug drug = drugRepository.findById(id).orElseThrow(()->new RuntimeException("Drug is not available for this ID: "+id));
        drug.setDrugStatus(DrugStatus.INACTIVE);
        Drug softDeleteDrug = drugRepository.save(drug);

        return drugMapper.toDTO(softDeleteDrug);

    }

    @Transactional
    public DrugResponse hardDeleteDrug(Long id){
        Drug drug = drugRepository.findById(id).orElseThrow(()->new RuntimeException("Drug is not available for this ID: "+id));

        drugRepository.delete(drug);

        return drugMapper.toDTO(drug);

    }

    //search operations----------------------------------------------------------------------
    public Page<DrugResponse> searchDrugs(DrugSearchCriteria criteria, Pageable pageable) {
        Specification<Drug> spec = DrugSpecification.search(criteria);
        return drugRepository.findAll(spec, pageable).map(drugMapper::toDTO);
    }

    
}
