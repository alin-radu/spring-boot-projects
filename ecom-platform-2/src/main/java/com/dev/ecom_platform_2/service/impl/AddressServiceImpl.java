package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.dtos.AddressDto;
import com.dev.ecom_platform_2.domain.dtos.AddressRequest;
import com.dev.ecom_platform_2.domain.entities.Address;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.AddressMapper;
import com.dev.ecom_platform_2.repositories.AddressRepository;
import com.dev.ecom_platform_2.service.AddressService;

import java.util.List;
import java.util.UUID;

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    // CREATE
    @Override
    public AddressDto createAddress(AddressRequest addressRequest, User user) {
        Address address = addressMapper.fromDto(addressRequest);
        address.setUser(user);

        List<Address> addressesList = user.getAddresses();
        addressesList.add(address);
        user.setAddresses(addressesList);
        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDto(savedAddress);
    }

    // READ
    @Override
    public AddressDto getAddressesById(UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId + "."));

        return addressMapper.toDto(address);
    }

    @Override
    public List<AddressDto> getAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public List<AddressDto> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();

        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }
    @Override
    public AddressDto updateAddress(UUID addressId, AddressRequest addressRequest) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId + "."));

        addressFromDatabase.setCity(addressRequest.getCity());
        addressFromDatabase.setZipCode(addressRequest.getZipCode());
        addressFromDatabase.setState(addressRequest.getState());
        addressFromDatabase.setCountry(addressRequest.getCountry());
        addressFromDatabase.setStreet(addressRequest.getStreet());
        addressFromDatabase.setBuildingName(addressRequest.getBuildingName());

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        return addressMapper.toDto(updatedAddress);
    }

    @Override
    public void deleteAddress(UUID addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId + "."));

        addressRepository.delete(addressFromDatabase);
    }
}