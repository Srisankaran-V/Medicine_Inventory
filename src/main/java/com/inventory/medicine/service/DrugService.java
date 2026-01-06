package com.inventory.medicine.service;

import com.inventory.medicine.dto.drug.CreateDrugRequest;
import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.dto.drug.UpdateDrugRequest;
import com.inventory.medicine.model.Drug;
import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;
import com.inventory.medicine.repository.DrugRepository;
import com.inventory.medicine.specification.DrugSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;


@Service
public class DrugService {

    private final DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository){
        this.drugRepository = drugRepository;
    }

    //Mappers-------------------------------------------------------------------------
    private DrugResponse toDTO(Drug drug){
        return DrugResponse.toConvertDrugResponseDTO(drug);
    }

    private List<DrugResponse> toDTOList(List<Drug> drugs){
        return drugs.stream().map(this::toDTO).toList();
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
        drug.setSellingPrice(createDrugRequest.sellingPrice());
        drug.setDrugStatus(DrugStatus.ACTIVE);

        Drug createdDrug = drugRepository.save(drug);
        System.out.println(createdDrug);

        return toDTO(createdDrug);

    }

    @Transactional(readOnly = true)
    public DrugResponse findById(Long id){
        return toDTO(drugRepository.findByIdForUpdate(id).orElseThrow(
                ()->new RuntimeException("Drug is not available for this ID: "+id)));
    }

    @Transactional(readOnly = true)
    public List<DrugResponse> findAllDrugs(){
        return toDTOList(drugRepository.findAll());
    }

    @Transactional
    public DrugResponse updateDrug(Long id, UpdateDrugRequest updateDrugRequest){
        Drug drug = drugRepository.findByIdForUpdate(id).orElseThrow(()-> new RuntimeException("Drug is not available for this id: "+id));
        if(updateDrugRequest.name() != null){drug.setName(updateDrugRequest.name());}
        if(updateDrugRequest.genericName() != null){drug.setGenericName(updateDrugRequest.genericName());}
        if(updateDrugRequest.drugCode() != null){drug.setDrugCode(updateDrugRequest.drugCode());}
        if(updateDrugRequest.drugClassification() != null){drug.setDrugClassification(updateDrugRequest.drugClassification());}
        if(updateDrugRequest.drugForm() != null){drug.setDrugForm(updateDrugRequest.drugForm());}
        if(updateDrugRequest.drugStatus() != null){drug.setDrugStatus(updateDrugRequest.drugStatus());}
        if(updateDrugRequest.quantityInStock() != null){drug.setQuantityInStock(updateDrugRequest.quantityInStock());}
        if(updateDrugRequest.minStockLevel() != null){drug.setMinStockLevel(updateDrugRequest.minStockLevel());}
        if(updateDrugRequest.expiryDate() != null){drug.setExpiryDate(updateDrugRequest.expiryDate());}
        if(updateDrugRequest.sellingPrice() != null){drug.setSellingPrice(updateDrugRequest.sellingPrice());}

        Drug updatedDrug = drugRepository.save(drug);
        return toDTO(updatedDrug);


    }

    @Transactional
    public DrugResponse softDeleteDrug(Long id){
        Drug drug = drugRepository.findById(id).orElseThrow(()->new RuntimeException("Drug is not available for this ID: "+id));
        drug.setDrugStatus(DrugStatus.INACTIVE);
        Drug softDeleteDrug = drugRepository.save(drug);

        return toDTO(softDeleteDrug);

    }

    @Transactional
    public DrugResponse hardDeleteDrug(Long id){
        Drug drug = drugRepository.findById(id).orElseThrow(()->new RuntimeException("Drug is not available for this ID: "+id));

        drugRepository.delete(drug);

        return toDTO(drug);

    }

    //Read operations----------------------------------------------------------------------
    public Page<DrugResponse> searchDrugs(
            String keyword,
            DrugClassification classification,
            DrugForm form,
            DrugStatus status,
            boolean lowStock,

            Integer quantity,
            String quantityOperation,

            String dateField,
            String dateOperation,
            LocalDateTime from,
            LocalDateTime to,
            int page,
            int size,
            String sort
    ) {
        String keywordLower =
                (keyword == null || keyword.isBlank()) ? null : keyword.toLowerCase();

        String[] sortArr = sort.split(",");
        Sort.Direction direction =
                sortArr.length > 1 && sortArr[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortArr[0]));

        Specification<Drug> spec = DrugSpecification.search(
                keywordLower,
                classification,
                form,
                status,
                lowStock,
                quantity,
                quantityOperation,
                dateField,
                dateOperation,
                from,
                to

        );

        return drugRepository.findAll(spec, pageable)
                .map(this::toDTO);
    }





    //To find Drug by drug_code
//    @Transactional(readOnly = true)
//    public Optional<DrugResponse> findByDrugCode(String drugCode){
//        return drugRepository.findByDrugCode(drugCode).map(this::toDTO);
//    }
//
//    //To find the list of drugs containing the name
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugName(String name){
//        return toDTOList(drugRepository.findByNameContainingIgnoreCase(name));
//    }
//
//    //To find the list of drugs containing the generic name
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByGenericName(String genericName){
//        return toDTOList(drugRepository.findByGenericNameContainingIgnoreCase(genericName));
//    }
//
//    //To find the list of drugs based on drug classification
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugClassification(DrugClassification drugClassification){
//        return toDTOList(drugRepository.findByDrugClassification(drugClassification));
//    }
//
//    //To find the list of drugs based on drug form
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugForm(DrugForm drugForm){
//        return toDTOList(drugRepository.findByDrugForm(drugForm));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByQuantityInStock(Integer quantity){
//        return toDTOList(drugRepository.findByQuantityInStock(quantity));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByQuantityInStockGreaterThanEqual(Integer quantity){
//        return toDTOList(drugRepository.findByQuantityInStockGreaterThanEqual(quantity));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByQuantityInStockLessThanEqual(Integer quantity){
//        return toDTOList(drugRepository.findByQuantityInStockLessThanEqual(quantity));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugStatus(DrugStatus drugStatus){
//        return toDTOList(drugRepository.findByDrugStatus(drugStatus));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugStatusAndQuantityInStockGreaterThanEqual(DrugStatus drugStatus, Integer quantity){
//        return toDTOList(drugRepository.findByDrugStatusAndQuantityInStockGreaterThanEqual(drugStatus, quantity));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByDrugStatusAndQuantityInStockLessThanEqual(DrugStatus drugStatus, Integer quantity){
//        return toDTOList(drugRepository.findByDrugStatusAndQuantityInStockLessThanEqual(drugStatus, quantity));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findLowQuantityInStock(){
//        return toDTOList(drugRepository.findLowQuantityInStock());
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findLowQuantityInStockByDrugStatus(DrugStatus drugStatus){
//        return toDTOList(drugRepository.findLowQuantityInStockByDrugStatus(drugStatus));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByExpiryDateBefore(LocalDateTime date){
//        return toDTOList(drugRepository.findByExpiryDateBefore(date));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByExpiryDateBetween(LocalDateTime from, LocalDateTime to){
//        return toDTOList(drugRepository.findByExpiryDateBetween(from, to));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to){
//        return toDTOList(drugRepository.findByCreatedAtBetween(from, to));
//    }
//
//    @Transactional(readOnly = true)
//    public List<DrugResponse> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to){
//        return toDTOList(drugRepository.findByUpdatedAtBetween(from, to));
//    }

    
}
