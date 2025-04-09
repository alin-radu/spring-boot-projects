package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.CartDto;
import com.dev.ecom_platform_2.domain.entities.Cart;

public interface CartMapper {
    CartDto toDto(Cart cart);
}
