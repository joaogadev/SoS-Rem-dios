package com.api.sosremedio.dto.response;

import com.api.sosremedio.model.PharmacyModel;

import java.time.LocalTime;
import java.util.UUID;

public record PharmacyResponse(
        UUID id,
        String ownerName,
        UUID ownerId,
        String name,
        String cnpj,
        String phone,
        String email,
        AddressResponse address,
        LocalTime openingTime,
        LocalTime closingTime
) {
    public static PharmacyResponse from(PharmacyModel pharmacy) {
        return new PharmacyResponse(
                pharmacy.getId(),
                pharmacy.getOwner().getName(),
                pharmacy.getOwner().getId(),
                pharmacy.getName(),
                pharmacy.getCnpj(),
                pharmacy.getPhone(),
                pharmacy.getEmail(),
                AddressResponse.from(pharmacy.getAddress()),
                pharmacy.getOpeningHours(),
                pharmacy.getClosingHours()
        );
    }
}
