package com.api.sosremedio.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePharmacyMedicineRequest(

        @NotNull(message = "O ID do medicamento é obrigatório")
        UUID medicineId,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal price,

        @Min(value = 0, message = "O estoque não pode ser negativo")
        Integer stock
) {
}