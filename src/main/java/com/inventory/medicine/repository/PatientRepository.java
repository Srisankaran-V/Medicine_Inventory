package com.inventory.medicine.repository;

//import com.inventory.medicine.entity.Patient;
import com.inventory.medicine.model.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<Patient> findByPatientCode(String patientCode);

    // Check for duplicates by looking into the joined User table
    boolean existsByUser_FullNameAndAgeAndUser_Phone(String name, Integer age, String phone);
}


