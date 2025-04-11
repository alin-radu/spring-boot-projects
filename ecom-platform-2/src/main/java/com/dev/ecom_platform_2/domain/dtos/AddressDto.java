package com.dev.ecom_platform_2.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDto {
    private UUID id;
    private String street;
    private String buildingName;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
