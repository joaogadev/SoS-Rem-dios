package com.api.sosremedio.DTO;

import com.api.sosremedio.model.UserRole;

import java.util.UUID;

public record MeResponse(
        UUID id,
        String email,
        UserRole role
) {
}
