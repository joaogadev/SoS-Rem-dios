package com.api.sosremedio.DTO;

import java.time.LocalTime;

public record PharmacyResponse(
        String name,
        String cnpj,
        String phone,
        String email,
        AddressResponse adress,
        LocalTime openingTime,
        LocalTime closingTime
) {
}
