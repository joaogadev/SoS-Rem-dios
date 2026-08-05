package com.api.sosremedio.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationRequest(

        @NotNull(message = "O item de estoque é obrigatório")
        UUID pharmacyMedicineId,

        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        int quantity
) {
}