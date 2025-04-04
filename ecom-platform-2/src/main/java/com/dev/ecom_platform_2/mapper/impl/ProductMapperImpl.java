package com.dev.ecom_platform_2.mapper.impl;

import com.dev.ecom_platform_2.domain.dtos.ProductRequest;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
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
    public Product fromDto(ProductRequest productRequest) {

        return modelMapper.map(productRequest, Product.class);
    }
    @Override
    public ProductDto toDto(Product product) {

        return modelMapper.typeMap(Product.class, ProductDto.class)
                .addMapping(src -> src.getCategory().getId(), ProductDto::setCategoryId)
                .map(product);
    }
}
