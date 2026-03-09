package com.inventory.medicine.repository;

import com.inventory.medicine.model.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends
        JpaRepository<Prescription, Long>,
        JpaSpecificationExecutor<Prescription> {

    /**
     * Prevents duplicate medical records for the same visit.
     */
    boolean existsByAppointmentId(Long appointmentId);

    /**
     * Optimized Fetch Join:
     * In one database hit, it retrieves the Prescription, all its Items,
     * and the Drug details (joining across 3 tables).
     */
    @Query("SELECT p FROM Prescription p " +
            "LEFT JOIN FETCH p.items i " +
            "LEFT JOIN FETCH i.drug " +
            "WHERE p.id = :id")
    Optional<Prescription> findByIdWithItems(@Param("id") Long id);

    /**
     * Retrieval by appointment, common for pharmacy dispensing.
     */
    Optional<Prescription> findByAppointmentId(Long appointmentId);
}