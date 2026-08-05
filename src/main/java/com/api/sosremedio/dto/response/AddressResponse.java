package com.api.sosremedio.dto.response;

import com.api.sosremedio.model.AddressModel;

public record AddressResponse(
        String zipcode,
        String state,
        String city,
        String neighborhood,
        String street,
        String number,
        String complement
) {
    public static AddressResponse from(AddressModel address) {
        return new AddressResponse(
                address.getZipcode(),
                address.getState(),
                address.getCity(),
                address.getNeighborhood(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement()
        );
    }
}
