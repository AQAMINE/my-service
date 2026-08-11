package com.myservice.infrastructure.adapters.out.persistence.repository;

import com.myservice.infrastructure.adapters.out.persistence.entity.ExternalAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataExternalAccountRepository extends JpaRepository<ExternalAccountEntity, UUID> {
    List<ExternalAccountEntity> findByUserId(UUID userId);
    List<ExternalAccountEntity> findByUserIdAndCategoryId(UUID userId, UUID categoryId);
    List<ExternalAccountEntity> findByUserIdAndProviderId(UUID userId, UUID providerId);

    Optional<ExternalAccountEntity> findByUserIdAndProviderIdAndUsername(UUID userId, UUID providerId, String username);
}
