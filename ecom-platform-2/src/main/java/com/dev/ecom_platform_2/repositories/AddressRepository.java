package com.dev.ecom_platform_2.repositories;

import com.dev.ecom_platform_2.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
