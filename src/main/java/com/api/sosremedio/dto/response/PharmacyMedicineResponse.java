package com.api.sosremedio.dto.response;

import com.api.sosremedio.model.AvailabilityStatus;
import com.api.sosremedio.model.ConfirmationSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PharmacyMedicineResponse(
        UUID id,
        UUID pharmacyId,
        String pharmacyName,
        UUID medicineId,
        String medicineName,
        String dosage,
        String pharmaceuticalForm,
        String manufacturer,
        boolean requiresPrescription,
        BigDecimal currentPrice,
        Integer stock,
        AvailabilityStatus availabilityStatus,
        LocalDateTime lastConfirmedAt,
        ConfirmationSource confirmationSource

) {
}
