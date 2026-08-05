package com.api.sosremedio.dto.request;

import com.api.sosremedio.model.AvailabilityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePharmacyMedicineRequest (

        @DecimalMin(value = "0.01", message = "The price must be greater than zero")
        BigDecimal currentPrice,

        @Min(value = 0, message = "The stock cannot be negative")
        Integer stock,

        @NotNull(message = "The availability status is required")
        AvailabilityStatus status
){
}
