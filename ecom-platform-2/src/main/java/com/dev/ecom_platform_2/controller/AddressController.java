package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.dtos.AddressDto;
import com.dev.ecom_platform_2.domain.dtos.AddressRequest;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.service.AddressService;
import com.dev.ecom_platform_2.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
public class AddressController {

    AuthUtil authUtil;

    AddressService addressService;

    // CREATE
    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressRequest addressRequest) {
        User user = authUtil.loggedInUser();
        AddressDto savedAddressDto = addressService.createAddress(addressRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddressDto);
    }

    // READ
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        List<AddressDto> addressList = addressService.getAddresses();

        return ResponseEntity.status(HttpStatus.FOUND).body(addressList);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable UUID addressId) {
        AddressDto addressDto = addressService.getAddressesById(addressId);

        return ResponseEntity.status(HttpStatus.OK).body(addressDto);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDto>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDto> addressList = addressService.getUserAddresses(user);

        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable UUID addressId,
            @RequestBody AddressRequest addressRequest) {
        AddressDto updatedAddress = addressService.updateAddress(addressId, addressRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedAddress);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable UUID addressId) {
        addressService.deleteAddress(addressId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
