package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.drug.DrugResponse;
import com.inventory.medicine.model.drug.Drug;
import org.springframework.stereotype.Component;


@Component
public class DrugMapper implements BaseMapper<Drug, DrugResponse> {

    @Override
    public DrugResponse toDTO(Drug drug){
        return new DrugResponse(
                drug.getId(),
                drug.getName(),
                drug.getGenericName(),
                drug.getDrugCode(),
                drug.getDrugClassification(),
                drug.getDrugForm(),
                drug.getQuantityInStock(),
                drug.getMinStockLevel(),
                drug.getExpiryDate(),
                drug.getPrice(),
                drug.getDrugStatus(),
                drug.getCreatedAt(),
                drug.getUpdatedAt()
        );
    }

}
