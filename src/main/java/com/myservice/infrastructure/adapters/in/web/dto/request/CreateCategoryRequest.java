package com.myservice.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    String name,

    @NotBlank(message = "Le slug est obligatoire")
    @Size(max = 50, message = "Le slug ne doit pas dépasser 50 caractères")
    String slug,

    String description
) {}
