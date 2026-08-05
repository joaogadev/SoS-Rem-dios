package com.api.sosremedio.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMedicineRequest(

        @NotBlank(message = "O nome do medicamento é obrigatório")
        @Size(max = 255)
        String name,

        @Size(max = 500)
        String description,

        @NotBlank(message = "O princípio ativo é obrigatório")
        @Size(max = 255)
        String activeIngredient,

        @NotBlank(message = "A dosagem é obrigatória")
        @Size(max = 255)
        String dosage,

        @NotBlank(message = "A forma farmacêutica é obrigatória")
        @Size(max = 255)
        String pharmaceuticalForm,

        @NotBlank(message = "O fabricante é obrigatório")
        @Size(max = 255)
        String manufacturer,

        boolean requiresPrescription
) {
}