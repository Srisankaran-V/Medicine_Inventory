package com.inventory.medicine.specification;

import com.inventory.medicine.model.appointment.Appointment;
import com.inventory.medicine.model.appointment.AppointmentStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class AppointmentSpecification {


    public static Specification<Appointment> search(
            Long doctorId,
            Long patientId,
            AppointmentStatus status,
            Instant fromDate,
            Instant toDate) {

        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (doctorId != null) {
                predicates = cb.and(predicates, cb.equal(root.get("doctor").get("id"), doctorId));
            }

            if (patientId != null) {
                predicates = cb.and(predicates, cb.equal(root.get("patient").get("id"), patientId));
            }

            if (status != null) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), status));
            }

            if (fromDate != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("startTime"), fromDate));
            }

            if (toDate != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("startTime"), toDate));
            }

            return predicates;
        };
    }
}