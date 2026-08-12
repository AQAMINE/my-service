package com.myservice.infrastructure.adapters.in.web.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExternalAccountResponse(
    UUID id,
    UUID userId,
    UUID categoryId,
    UUID providerId,
    String fullName,
    String username,
    String email,
    String link,
    String description,
    Boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}