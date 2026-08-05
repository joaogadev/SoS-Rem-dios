package com.api.sosremedio.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

public record CreatePharmacyRequest(
        @NotBlank(message = "O nome da farmácia não pode ser vazio")
        @Size(
                min = 3, max = 255,
                message = "O campo deve ter no mínimo 3 caracteres"
        )
        String name,
        @NotBlank(message = "O CNPJ não pode ser vazio")
        @Pattern(
                regexp = "^\\+?[0-9]{14}$",
                message = "O CNPJ deve conter exatamente 14 números"
        )
        String cnpj,

        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Informe um telefone válido, contendo apenas números e opcionalmente o sinal de +"
        )
        String phone,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um email válido")
        @Size(max = 255, message = "Email não deve ultrapassar 255 caracteres")
        String email,

        @NotNull(message = "O endereço não pode ser vazio")
        @Valid
        CreateAddressRequest address,

        @NotNull(message = "Insira o horário de abertura")
        @JsonFormat(pattern = "HH:mm")
        LocalTime openingHours,

        @NotNull(message = "Insira o horário de fechamento")
        @JsonFormat(pattern = "HH:mm")
        LocalTime closingHours
) {
}
