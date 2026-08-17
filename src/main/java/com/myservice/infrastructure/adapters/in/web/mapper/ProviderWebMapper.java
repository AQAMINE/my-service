package com.myservice.infrastructure.adapters.in.web.mapper;

import com.myservice.domain.model.Provider;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateProviderRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.ProviderResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProviderWebMapper {

    public Provider toDomain(CreateProviderRequest request, UUID userId) {
        if (request == null) return null;
        return Provider.builder()
                .userId(userId)
                .name(request.name())
                .slug(request.slug())
                .websiteUrl(request.websiteUrl())
                .color(request.color())
                .logoUrl(request.logoUrl())
                .isActive(true)
                .build();
    }

    public ProviderResponse toResponse(Provider domain) {
        if (domain == null) return null;
        return new ProviderResponse(
                domain.getId(),
                domain.getUserId(),
                domain.getName(),
                domain.getSlug(),
                domain.getWebsiteUrl(),
                domain.getColor(),
                domain.getLogoUrl(),
                domain.getIsActive(),
                domain.getCreatedAt()
        );
    }
}