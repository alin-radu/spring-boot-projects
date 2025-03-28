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
public class ProductRequestDto {

    private UUID id;

    @NotBlank(message = "Product name is required.")
    @Size(min = 2, max = 50, message = "Product name must be between {min} and {max} characters.")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Category name can only contain letters, numbers, spaces, and hyphens.")
    private String name;

    @NotBlank
    @Size(min = 6, message = "Product description must contain atleast 6 characters")
    private String description;

    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
