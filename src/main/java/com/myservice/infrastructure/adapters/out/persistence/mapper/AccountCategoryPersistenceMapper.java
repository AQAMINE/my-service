package com.myservice.infrastructure.adapters.out.persistence.mapper;

import com.myservice.domain.model.AccountCategory;
import com.myservice.infrastructure.adapters.out.persistence.entity.AccountCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountCategoryPersistenceMapper {

    public AccountCategory toDomain(AccountCategoryEntity entity) {
        if (entity == null) return null;
        return AccountCategory.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AccountCategoryEntity toEntity(AccountCategory domain) {
        if (domain == null) return null;
        return AccountCategoryEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName())
                .slug(domain.getSlug())
                .description(domain.getDescription())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}