package com.myservice.infrastructure.adapters.in.web.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryResponse(
    UUID id,
    UUID userId,
    String name,
    String slug,
    String description,
    OffsetDateTime createdAt
) {}