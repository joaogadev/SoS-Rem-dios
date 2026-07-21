package com.api.sosremedio.DTO;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
