package com.api.sosremedio.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatedAddressRequest(
        @NotBlank(message = "O CEP não pode estar vazio")
        @Pattern(
                regexp = "^[0-9]{8}$",
                message = "O cnpj deve conter exatamente 8 números"

        )
        String zipcode,

        @NotBlank
        @Size(
                min = 2,
                max = 2,
                message = "Apenas 2 caracteres são permitidos para o estado"
        )
        String state,

        @NotBlank
        @Size(max = 255)
        String city,
        @NotBlank
        @Size(max = 255)
        String neighborhood,

        @NotBlank
        @Size(max = 255)
        String street,

        @NotBlank
        @Size(max = 255)
        String number,

        @Size(max = 255)
        String complement
) {
}
