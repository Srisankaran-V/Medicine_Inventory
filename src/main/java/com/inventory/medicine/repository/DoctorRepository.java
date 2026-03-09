package com.inventory.medicine.repository;

import com.inventory.medicine.model.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

    // Check if license exists before creating (Industry Standard)
    boolean existsByLicenseNumber(String licenseNumber);

    // Find a specific doctor by their legal license
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumberAndIdNot(String licenseNumber, Long id);
}