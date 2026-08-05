package com.api.sosremedio.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email não pode ser vazio")
        @Email(message = "Informe um email válido")
        String email,

        @NotBlank(message = "A senha não pode estar vazia")
        String password
) {
}
