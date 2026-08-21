package com.myservice.infrastructure.adapters.in.web.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean enabled
) {}