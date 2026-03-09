package com.inventory.medicine.dto.auth;

public record AuthResponse(
        String token,
        String fullName,
        String role,
        Long profileId // ID of Doctor or Patient entity
) {}