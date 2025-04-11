package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.AddressDto;
import com.dev.ecom_platform_2.domain.dtos.AddressRequest;
import com.dev.ecom_platform_2.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressDto createAddress(AddressRequest addressRequest, User user);
    AddressDto getAddressesById(UUID addressId);
    List<AddressDto> getAddresses();
    List<AddressDto> getUserAddresses(User user);
    AddressDto updateAddress(UUID addressId, AddressRequest addressRequest);
    void deleteAddress(UUID addressId);
}
