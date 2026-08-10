package com.myservice.infrastructure.adapters.out.persistence.repository;

import com.myservice.infrastructure.adapters.out.persistence.entity.AccountCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataCategoryRepository extends JpaRepository<AccountCategoryEntity, UUID> {
    @Query("SELECT c FROM AccountCategoryEntity c WHERE c.userId IS NULL OR c.userId = :userId")
    List<AccountCategoryEntity> findAllAvailableForUser(@Param("userId") UUID userId);
}
