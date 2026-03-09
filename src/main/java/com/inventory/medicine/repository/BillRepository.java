package com.inventory.medicine.repository;

import com.inventory.medicine.model.billing.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    Optional<Bill> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    Optional<Bill> findByBillNumber(String billNumber);
}