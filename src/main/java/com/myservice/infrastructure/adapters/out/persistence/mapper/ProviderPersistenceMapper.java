package com.myservice.infrastructure.adapters.out.persistence.mapper;

import com.myservice.domain.model.Provider;
import com.myservice.infrastructure.adapters.out.persistence.entity.ProviderEntity;
import org.springframework.stereotype.Component;

@Component
public class ProviderPersistenceMapper {

    public Provider toDomain(ProviderEntity entity) {
        if (entity == null) return null;
        return Provider.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .websiteUrl(entity.getWebsiteUrl())
                .color(entity.getColor())
                .logoUrl(entity.getLogoUrl())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ProviderEntity toEntity(Provider domain) {
        if (domain == null) return null;
        return ProviderEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName())
                .slug(domain.getSlug())
                .websiteUrl(domain.getWebsiteUrl())
                .color(domain.getColor())
                .logoUrl(domain.getLogoUrl())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}