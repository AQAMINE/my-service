package com.myservice.infrastructure.adapters.in.web.dto.response;

public record AccessTokenResponse(
        String accessToken,
        long expiresIn,
        String tokenType
) {}