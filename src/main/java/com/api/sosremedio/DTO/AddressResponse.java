package com.api.sosremedio.DTO;

public record AddressResponse(
        String zipcode,
        String state,
        String city,
        String neighborhood,
        String street,
        String number,
        String complement
) {
}
