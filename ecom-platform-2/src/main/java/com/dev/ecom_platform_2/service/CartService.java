package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.CartDto;
import com.dev.ecom_platform_2.domain.dtos.CartItemDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface CartService {
    CartDto addProductToCart(UUID productId, Integer quantity);
    List<CartDto> getAllCarts();
    CartDto getCartByUserEmailAndCartId(String userEmail, UUID cartId);

    @Transactional
    CartDto updateProductQuantityInCart(UUID productId, Integer quantity);

    void deleteProductFromCart(UUID cartId, UUID productId);

    void updateProductInCarts(UUID cartId, UUID productId);
    void createOrUpdateCartWithItems(List<CartItemDto> cartItems);
}
