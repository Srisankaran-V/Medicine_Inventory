package com.inventory.medicine.mapper;

import com.inventory.medicine.dto.doctor.DoctorResponse;
import com.inventory.medicine.model.doctor.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper implements BaseMapper<Doctor, DoctorResponse> {

    @Override
    public DoctorResponse toDTO(Doctor doctor) {
        if (doctor == null) return null;

        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getFullName(),
                doctor.getSpecialization(),
                doctor.getLicenseNumber(),
                doctor.getUser().getPhone(),
                doctor.getUser().getEmail(),
                doctor.getActive(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt()
        );
    }
}