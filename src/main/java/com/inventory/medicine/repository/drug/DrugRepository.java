package com.inventory.medicine.repository.drug;

import com.inventory.medicine.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    Optional<Drug> findByDrugCode(String drugCode);
    List<Drug> findByNameContainingIgnoreCase(String name);
    List<Drug> findByGenericNameContainingIgnoreCase(String genericName);
}
