package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.dtos.CartDto;
import com.dev.ecom_platform_2.domain.dtos.CartItemDto;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
import com.dev.ecom_platform_2.domain.entities.Cart;
import com.dev.ecom_platform_2.domain.entities.CartItem;
import com.dev.ecom_platform_2.domain.entities.Product;
import com.dev.ecom_platform_2.domain.entities.User;
import com.dev.ecom_platform_2.exception.APIException;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.CartMapper;
import com.dev.ecom_platform_2.mapper.ProductMapper;
import com.dev.ecom_platform_2.repositories.CartItemRepository;
import com.dev.ecom_platform_2.repositories.CartRepository;
import com.dev.ecom_platform_2.repositories.ProductRepository;
import com.dev.ecom_platform_2.service.CartService;
import com.dev.ecom_platform_2.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private final AuthUtil authUtil;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, AuthUtil authUtil, CartMapper cartMapper, ProductMapper productMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.authUtil = authUtil;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CartDto addProductToCart(UUID productId, Integer quantity) {
        if (!authUtil.isAuthenticatedUser()) {
            throw new APIException("Please authenticate to use this functionality.");
        }

        Cart cart = getCartByUserEmail(authUtil.loggedInUserEmail());
        if (cart == null) {
            cart = createCartByUserEmail(authUtil.loggedInUser());
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId + "."));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getId(), productId);
        if (cartItem != null) {
            throw new APIException("Product " + product.getName() + " already exists in the cart+ \".\"");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getName() + " is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        cart.addCartItem(newCartItem);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDto cartDto = cartMapper.toDto(cart);
        List<ProductDto> productDtoList = getProductDtoList(cart);
        cartDto.setProducts(productDtoList);

        return cartDto;

    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            return Collections.emptyList();
        }

        return carts.stream()
                .map(cart -> {
                    CartDto cartDTO = cartMapper.toDto(cart);
                    List<ProductDto> products = getProductDtoList(cart);
                    cartDTO.setProducts(products);

                    return cartDTO;
                })
                .collect(Collectors.toList());
    }
    @Override
    public CartDto getCartByUserEmailAndCartId(String userEmail, UUID cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(userEmail, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found with id " + cartId + ".");
        }

        CartDto cartDTO = cartMapper.toDto(cart);
        List<ProductDto> productDtoList = getProductDtoList(cart);
        cartDTO.setProducts(productDtoList);

        return cartDTO;
    }

    @Transactional
    @Override
    public CartDto updateProductQuantityInCart(UUID productId, Integer quantity) {

        String userEmail = authUtil.loggedInUserEmail();
        Cart userCart = cartRepository.findCartByEmail(userEmail);
        UUID cartId = userCart.getId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + cartId + "."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId + "."));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getName() + " is not available.");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getName() + " not available in the cart!");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if (updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getId());
        }

        CartDto cartDTO = cartMapper.toDto(cart);
        List<ProductDto> productDtoList = getProductDtoList(cart);
        cartDTO.setProducts(productDtoList);

        return cartDTO;
    }

    @Transactional
    @Override
    public void deleteProductFromCart(UUID cartId, UUID productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + cartId + "."));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product not found with id " + productId + ".");
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
    }

    @Override
    public void updateProductInCarts(UUID cartId, UUID productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + cartId + "."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId + "."));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product " + product.getName() + " not available in the cart!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public void createOrUpdateCartWithItems(List<CartItemDto> cartItems) {
        if (!authUtil.isAuthenticatedUser()) {
            throw new APIException("Please authenticate to use this functionality.");
        }
        String userEmail = authUtil.loggedInUserEmail();

        Cart existingCart = cartRepository.findCartByEmail(userEmail);
        if (existingCart == null) {
            existingCart = new Cart();
            existingCart.setTotalPrice(0.00);
            existingCart.setUser(authUtil.loggedInUser());
            existingCart = cartRepository.save(existingCart);
        } else {
            cartItemRepository.deleteAllByCartId(existingCart.getId());
        }

        double totalPrice = 0.00;

        for (CartItemDto cartItemDTO : cartItems) {
            UUID productId = cartItemDTO.getId();
            Integer quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId + "."));
            totalPrice += product.getSpecialPrice() * quantity;

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(existingCart);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
        }

        existingCart.setTotalPrice(totalPrice);
        cartRepository.save(existingCart);
    }

    // UTIL
    private Cart getCartByUserEmail(String userEmail) {
        return cartRepository.findCartByEmail(userEmail);
    }

    private Cart createCartByUserEmail(User user) {
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    private List<ProductDto> getProductDtoList(Cart cart) {
        return cart.getCartItems().stream()
                .map(item -> {
                    ProductDto productDto = productMapper.toDto(item.getProduct());
                    productDto.setQuantity(item.getQuantity());
                    return productDto;
                })
                .collect(Collectors.toList());
    }
}
