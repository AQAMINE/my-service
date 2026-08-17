package com.myservice.infrastructure.adapters.in.web.mapper;

import com.myservice.domain.model.AccountCategory;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateCategoryRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryWebMapper {

    public AccountCategory toDomain(CreateCategoryRequest request, UUID userId) {
        if (request == null) return null;
        return AccountCategory.builder()
                .userId(userId)
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .build();
    }

    public CategoryResponse toResponse(AccountCategory domain) {
        if (domain == null) return null;
        return new CategoryResponse(
                domain.getId(),
                domain.getUserId(),
                domain.getName(),
                domain.getSlug(),
                domain.getDescription(),
                domain.getCreatedAt()
        );
    }
}