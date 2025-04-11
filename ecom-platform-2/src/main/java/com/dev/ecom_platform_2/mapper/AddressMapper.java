package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.AddressDto;
import com.dev.ecom_platform_2.domain.dtos.AddressRequest;
import com.dev.ecom_platform_2.domain.entities.Address;

public interface AddressMapper {
    Address fromDto(AddressRequest addressRequest);
    AddressDto toDto(Address address);
}
