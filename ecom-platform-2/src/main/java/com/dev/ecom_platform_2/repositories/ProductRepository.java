package com.dev.ecom_platform_2.repositories;

import com.dev.ecom_platform_2.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByCategoryId(UUID categoryId, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

}
