package com.inventory.medicine.repository;

import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.doctor.Doctor;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

    // Check if license exists before creating (Industry Standard)
    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUserId(Long id);



    // Find a specific doctor by their legal license
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumberAndIdNot(String licenseNumber, Long id);

    boolean existsByUser_FullNameAndAgeAndUser_PhoneAndUser_Email(@NotBlank(message = "Patient name is required") @Size(min = 2, max = 100, message = "Name must be between 2 to 100 characters") String name, @NotNull(message = "Age is required") @Min(value = 0, message = "Age cannot be negative") Integer age, @NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "Invalid phone number format") String phone, @Email(message = "Invalid email format") String email);
}