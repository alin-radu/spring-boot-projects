package com.dev.ecom_platform_2.repositories;

import com.dev.ecom_platform_2.domain.entities.AppRole;
import com.dev.ecom_platform_2.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(AppRole appRole);
}
