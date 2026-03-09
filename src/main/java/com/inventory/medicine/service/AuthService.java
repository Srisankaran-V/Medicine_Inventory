package com.inventory.medicine.service;


import com.inventory.medicine.config.JwtService;
import com.inventory.medicine.dto.auth.AuthResponse;
import com.inventory.medicine.dto.auth.RegisterRequest;
import com.inventory.medicine.model.auth.Role;
import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.patient.Patient;
import com.inventory.medicine.repository.AuthRepository;
import com.inventory.medicine.repository.DoctorRepository;
import com.inventory.medicine.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Create the User (The Security Identity)
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        Long profileId = null;

        // 2. Handle Profile Creation based on Role
        if (request.role() == Role.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .specialization(request.specialization())
                    .licenseNumber(request.licenseNumber())
                    .build();
            profileId = doctorRepository.save(doctor).getId();
        } else if (request.role() == Role.PATIENT) {
            Patient patient = Patient.builder()
                    .user(user)
                    .patientCode(request.patientCode())
                    .build();
            profileId = patientRepository.save(patient).getId();
        } else {
            authRepository.save(user); // For Admin/Receptionist
        }

        // 3. Generate JWT
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getFullName(), user.getRole().name(), profileId);
    }
}