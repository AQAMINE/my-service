package com.myservice.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateExternalAccountRequest(
    @NotNull(message = "La catégorie est obligatoire")
    UUID categoryId,

    @NotNull(message = "Le provider est obligatoire")
    UUID providerId,

    String fullName,
    String username,
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    String rawPassword,

    String link,
    String description
) {}