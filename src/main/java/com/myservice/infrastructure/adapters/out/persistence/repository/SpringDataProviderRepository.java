package com.myservice.infrastructure.adapters.out.persistence.repository;

import com.myservice.infrastructure.adapters.out.persistence.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataProviderRepository extends JpaRepository<ProviderEntity, UUID> {
    @Query("SELECT p FROM ProviderEntity p WHERE p.userId IS NULL OR p.userId = :userId")
    List<ProviderEntity> findAllAvailableForUser(@Param("userId") UUID userId);
}