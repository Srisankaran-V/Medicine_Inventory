package com.inventory.medicine.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient not found with ID: " + id);
    }
}