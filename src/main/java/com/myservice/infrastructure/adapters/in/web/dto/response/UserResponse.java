package com.myservice.infrastructure.adapters.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.UUID;

public record UserResponse(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("enabled") Boolean enabled
) implements Serializable {
    private static final long serialVersionUID = 1L;
}