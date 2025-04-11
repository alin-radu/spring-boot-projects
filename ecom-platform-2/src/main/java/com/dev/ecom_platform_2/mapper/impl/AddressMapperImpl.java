package com.dev.ecom_platform_2.mapper.impl;

import com.dev.ecom_platform_2.domain.dtos.AddressDto;
import com.dev.ecom_platform_2.domain.dtos.AddressRequest;
import com.dev.ecom_platform_2.domain.entities.Address;
import com.dev.ecom_platform_2.mapper.AddressMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapperImpl implements AddressMapper {
    private final ModelMapper modelMapper;

    public AddressMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public Address fromDto(AddressRequest addressRequest) {
        return modelMapper.map(addressRequest, Address.class);
    }
    @Override
    public AddressDto toDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }
}
