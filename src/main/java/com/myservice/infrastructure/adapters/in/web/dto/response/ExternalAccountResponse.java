package com.myservice.infrastructure.adapters.in.web.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExternalAccountResponse(
        UUID id,
        UUID userId,
        CategorySummaryResponse category,
        ProviderSummaryResponse provider,
        String fullName,
        String username,
        String email,
        String link,
        String description,
        Boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public record CategorySummaryResponse(
            UUID id,
            String name,
            String slug
    ) {}

    public record ProviderSummaryResponse(
            UUID id,
            String name,
            String slug,
            String logoUrl,
            String color,
            String websiteUrl
    ) {}
}