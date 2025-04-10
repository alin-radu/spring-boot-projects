package com.dev.ecom_platform_2.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDto {
    private UUID id;
    private Integer quantity;
}
