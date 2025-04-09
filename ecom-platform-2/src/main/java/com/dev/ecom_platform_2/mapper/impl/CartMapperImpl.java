package com.dev.ecom_platform_2.mapper.impl;

import com.dev.ecom_platform_2.domain.dtos.CartDto;
import com.dev.ecom_platform_2.domain.entities.Cart;
import com.dev.ecom_platform_2.mapper.CartMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapperImpl implements CartMapper {
    private final ModelMapper modelMapper;

    public CartMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto toDto(Cart cart) {

        return modelMapper.map(cart, CartDto.class);
    }
}
