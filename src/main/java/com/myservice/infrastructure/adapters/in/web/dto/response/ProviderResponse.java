package com.myservice.infrastructure.adapters.in.web.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProviderResponse(
    UUID id,
    UUID userId,
    String name,
    String slug,
    String websiteUrl,
    String color,
    String logoUrl,
    Boolean isActive,
    OffsetDateTime createdAt
) {}