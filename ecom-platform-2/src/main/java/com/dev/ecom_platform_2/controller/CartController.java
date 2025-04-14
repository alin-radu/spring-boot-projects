package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.config.AppConstants;
import com.dev.ecom_platform_2.domain.dtos.CartDto;
import com.dev.ecom_platform_2.domain.entities.Cart;
import com.dev.ecom_platform_2.exception.APIException;
import com.dev.ecom_platform_2.repositories.CartRepository;
import com.dev.ecom_platform_2.service.CartService;
import com.dev.ecom_platform_2.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
public class CartController {

    private final CartService cartService;

    private final CartRepository cartRepository;

    private final AuthUtil authUtil;

    public CartController(CartService cartService, CartRepository cartRepository, AuthUtil authUtil) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
    }

    // CREATE
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(
            @PathVariable UUID productId, @PathVariable Integer quantity) {

        CartDto savedCart = cartService.addProductToCart(productId, quantity);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    // READ
    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        List<CartDto> cartDtos = cartService.getAllCarts();

        return ResponseEntity.status(HttpStatus.FOUND).body(cartDtos);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDto> getCartByUserId() {
        if (!authUtil.isAuthenticatedUser()) {
            throw new APIException("Please authenticate to use this functionality.");
        }

        String userEmail = authUtil.loggedInUserEmail();
        Cart cart = cartRepository.findCartByEmail(userEmail);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UUID cartId = cart.getId();
        CartDto cartDto = cartService.getCartByUserEmailAndCartId(userEmail, cartId);

        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    // UPDATE
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDto> updateProductQuantityInCart(
            @PathVariable UUID productId, @PathVariable String operation) {
        var quantity = operation.equalsIgnoreCase(AppConstants.CART_ACTION_DELETE) ? -1 : 1;
        CartDto cartDto = cartService.updateProductQuantityInCart(productId, quantity);

        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    // DELETE
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<Void> deleteProductFromCart(
            @PathVariable UUID cartId, @PathVariable UUID productId) {
        cartService.deleteProductFromCart(cartId, productId);

        System.out.println("test");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
