package com.inventory.medicine.service;

import com.inventory.medicine.config.JwtService;
import com.inventory.medicine.dto.auth.AuthResponse;
import com.inventory.medicine.dto.auth.LoginRequest;
import com.inventory.medicine.dto.auth.RegisterRequest;
import com.inventory.medicine.model.auth.Role;
import com.inventory.medicine.model.auth.User;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.doctor.Specialization;
import com.inventory.medicine.model.patient.Patient;
import com.inventory.medicine.repository.AuthRepository;
import com.inventory.medicine.repository.DoctorRepository;
import com.inventory.medicine.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (authRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        User savedUser = authRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getFullName(),
                savedUser.getRole().name(),
                null
        );
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = authRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long profileId = null;

        if (user.getRole() == Role.DOCTOR) {
            profileId = doctorRepository.findByUser(user)
                    .map(Doctor::getId)
                    .orElse(null);
        } else if (user.getRole() == Role.PATIENT) {
            profileId = patientRepository.findByUser(user)
                    .map(Patient::getId)
                    .orElse(null);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getFullName(),
                user.getRole().name(),
                profileId
        );
    }
}