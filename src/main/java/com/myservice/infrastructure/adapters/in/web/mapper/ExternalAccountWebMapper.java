package com.myservice.infrastructure.adapters.in.web.mapper;

import com.myservice.domain.model.ExternalAccount;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateExternalAccountRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.ExternalAccountResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExternalAccountWebMapper {

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
        return new ExternalAccountResponse(
                domain.getId(),
                domain.getUserId(),
                domain.getCategoryId(),
                domain.getProviderId(),
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