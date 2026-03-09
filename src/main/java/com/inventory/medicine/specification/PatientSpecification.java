package com.inventory.medicine.specification;

import com.inventory.medicine.dto.patient.PatientSearchCriteria;
import com.inventory.medicine.model.patient.Patient;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class PatientSpecification {

    public static Specification<Patient> search(PatientSearchCriteria criteria){
        //cb - CriteriaBuilder
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // JOIN with User table to access fullName and phone
            Join<Object, Object> userJoin = root.join("user");

            /* ---------- Keyword Search ----------
               SQL: ... WHERE (LOWER(name) LIKE %k% OR patient_code LIKE %k% OR phone LIKE %k%)
            */
            if (criteria.keyword() != null && !criteria.keyword().isBlank()) {
                String pattern = "%" + criteria.keyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(userJoin.get("fullName")), pattern), // From User
                        cb.like(cb.lower(userJoin.get("phone")), pattern),    // From User
                        cb.like(cb.lower(root.get("patientCode")), pattern)   // From Patient
                ));
            }



            /* ---------- Gender Filter ----------
               SQL: ... AND gender = 'MALE'
            */
            if (criteria.gender() != null){
                predicates.add(cb.equal(root.get("gender"), criteria.gender()));
            }

            /* ---------- Blood Group Filter ----------
               SQL: ... AND blood_group = 'O+'
            */

            if (criteria.bloodGroup() != null){
                predicates.add(cb.equal(root.get("bloodGroup"), criteria.bloodGroup()));
            }

            /* ---------- Age Range Filter ----------
               SQL: ... AND age >= 18 AND age <= 65
            */
            if (criteria.minAge() != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), criteria.minAge()));
            }
            if (criteria.maxAge() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), criteria.maxAge()));
            }

            if(criteria.active() != null){
                predicates.add(cb.equal(root.get("active"), criteria.active()));
            }

            /* ---------- Date Range Filter ----------
               SQL: ... AND created_at BETWEEN 'from' AND 'to'
            */
            if (criteria.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.from()));
            }
            if (criteria.to() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.to()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));


        });
    }
}
