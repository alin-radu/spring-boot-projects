package com.dev.ecom_platform_2.mapper.impl;

import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import com.dev.ecom_platform_2.domain.entities.Product;
import com.dev.ecom_platform_2.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {

    private final ModelMapper modelMapper;
    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Product fromDto(ProductRequestDto productRequestDto) {

        return modelMapper.map(productRequestDto, Product.class);
    }
    @Override
    public ProductResponseDto toDto(Product product) {

        return modelMapper.typeMap(Product.class, ProductResponseDto.class)
                .addMapping(src -> src.getCategory().getId(), ProductResponseDto::setCategoryId)
                .map(product);
    }
}
