package com.myservice.infrastructure.adapters.out.persistence.mapper;

import com.myservice.domain.model.ExternalAccount;
import com.myservice.infrastructure.adapters.out.persistence.entity.ExternalAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class ExternalAccountPersistenceMapper {

    public ExternalAccount toDomain(ExternalAccountEntity entity) {
        if (entity == null) return null;
        return ExternalAccount.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .categoryId(entity.getCategoryId())
                .providerId(entity.getProviderId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .encryptedPassword(entity.getEncryptedPassword())
                .encryptionIv(entity.getEncryptionIv())
                .link(entity.getLink())
                .description(entity.getDescription())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ExternalAccountEntity toEntity(ExternalAccount domain) {
        if (domain == null) return null;
        return ExternalAccountEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .categoryId(domain.getCategoryId())
                .providerId(domain.getProviderId())
                .fullName(domain.getFullName())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .encryptedPassword(domain.getEncryptedPassword())
                .encryptionIv(domain.getEncryptionIv())
                .link(domain.getLink())
                .description(domain.getDescription())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}