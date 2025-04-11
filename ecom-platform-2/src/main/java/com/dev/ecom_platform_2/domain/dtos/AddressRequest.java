package com.dev.ecom_platform_2.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequest {
    private UUID id;

    @Size(min = 2, max = 30, message = "Street name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Street name can only contain letters, numbers, spaces, and hyphens.")
    private String street;

    @Size(min = 2, max = 30, message = "Building Name name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Building Name name can only contain letters, numbers, spaces, and hyphens.")
    private String buildingName;

    @NotBlank(message = "City name is required.")
    @Size(min = 2, max = 30, message = "City name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "City name can only contain letters, numbers, spaces, and hyphens.")
    private String city;

    @NotBlank(message = "State name is required.")
    @Size(min = 2, max = 30, message = "State name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "State name can only contain letters, numbers, spaces, and hyphens.")
    private String state;

    @NotBlank(message = "Country name is required.")
    @Size(min = 2, max = 30, message = "Country name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Country name can only contain letters, numbers, spaces, and hyphens.")
    private String country;

    @Size(min = 2, max = 30, message = "Zip Code name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Zip Code name can only contain letters, numbers, spaces, and hyphens.")
    private String zipCode;
}
