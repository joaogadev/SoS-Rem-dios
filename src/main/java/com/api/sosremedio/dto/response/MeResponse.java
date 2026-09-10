package com.api.sosremedio.dto.response;

import com.api.sosremedio.model.UserRole;

import java.util.UUID;

public record MeResponse(
        UUID id,
        String email,
        UserRole role
) {
}
