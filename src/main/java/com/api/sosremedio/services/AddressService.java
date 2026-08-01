package com.api.sosremedio.services;

import com.api.sosremedio.DTO.AddressResponse;
import com.api.sosremedio.DTO.CreatedAddressRequest;
import com.api.sosremedio.model.AddressModel;
import com.api.sosremedio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressResponse addAddress(CreatedAddressRequest address) {
        AddressModel newAddress = new AddressModel(
                address.zipcode(),
                address.state(),
                address.city(),
                address.neighborhood(),
                address.street(),
                address.number(),
                address.complement()
        );

        AddressModel savedAddress = addressRepository.save(newAddress);

        return AddressResponse.from(savedAddress);
    }

}
