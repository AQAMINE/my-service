package com.myservice.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProviderRequest(
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    String name,

    @NotBlank(message = "Le slug est obligatoire")
    @Size(max = 100)
    String slug,

    @Size(max = 2048)
    String websiteUrl,

    @Size(max = 50)
    String color,

    @Size(max = 2048)
    String logoUrl
) {}