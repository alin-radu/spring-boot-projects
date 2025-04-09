package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.CartDto;

import java.util.List;
import java.util.UUID;

public interface CartService {
    CartDto addProductToCart(UUID productId, Integer quantity);
    List<CartDto> getAllCarts();
    CartDto getCartByUserEmailAndCartId(String userEmail, UUID cartId);
}
