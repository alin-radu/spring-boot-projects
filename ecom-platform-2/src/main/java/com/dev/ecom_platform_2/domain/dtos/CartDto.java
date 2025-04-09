package com.dev.ecom_platform_2.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDto {
    private UUID id;
    private List<ProductDto> products = new ArrayList<>();
    private Double totalPrice = 0.0;
}
