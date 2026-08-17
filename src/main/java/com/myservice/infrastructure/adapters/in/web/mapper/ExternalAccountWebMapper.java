package com.myservice.infrastructure.adapters.in.web.mapper;

import com.myservice.domain.model.ExternalAccount;
import com.myservice.domain.ports.out.AccountCategoryRepositoryPort;
import com.myservice.domain.ports.out.ProviderRepositoryPort;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateExternalAccountRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.ExternalAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExternalAccountWebMapper {

    private final AccountCategoryRepositoryPort categoryRepositoryPort;
    private final ProviderRepositoryPort providerRepositoryPort;

    public ExternalAccount toDomain(CreateExternalAccountRequest request, UUID userId) {
        if (request == null) return null;
        return ExternalAccount.builder()
                .userId(userId)
                .categoryId(request.categoryId())
                .providerId(request.providerId())
                .fullName(request.fullName())
                .username(request.username())
                .email(request.email())
                .link(request.link())
                .description(request.description())
                .isActive(true)
                .build();
    }

    public ExternalAccountResponse toResponse(ExternalAccount domain) {
        if (domain == null) return null;

        // 1. Récupération des détails de la catégorie
        ExternalAccountResponse.CategorySummaryResponse categorySummary = null;
        if (domain.getCategoryId() != null) {
            categorySummary = categoryRepositoryPort.findById(domain.getCategoryId())
                    .map(c -> new ExternalAccountResponse.CategorySummaryResponse(c.getId(), c.getName(), c.getSlug()))
                    .orElse(null);
        }

        // 2. Récupération des détails du provider
        ExternalAccountResponse.ProviderSummaryResponse providerSummary = null;
        if (domain.getProviderId() != null) {
            providerSummary = providerRepositoryPort.findById(domain.getProviderId())
                    .map(p -> new ExternalAccountResponse.ProviderSummaryResponse(
                            p.getId(), p.getName(), p.getSlug(), p.getLogoUrl(), p.getColor(), p.getWebsiteUrl()))
                    .orElse(null);
        }

        return new ExternalAccountResponse(
                domain.getId(),
                domain.getUserId(),
                categorySummary,
                providerSummary,
                domain.getFullName(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getLink(),
                domain.getDescription(),
                domain.getIsActive(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}