package com.myservice.infrastructure.adapters.in.web.controller;

import com.myservice.application.ports.in.AccountCategoryUseCase;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateCategoryRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.CategoryResponse;
import com.myservice.infrastructure.adapters.in.web.mapper.CategoryWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final AccountCategoryUseCase categoryUseCase;
    private final CategoryWebMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAvailableCategories(
            @RequestHeader("X-User-Id") UUID userId) {
        var categories = categoryUseCase.getAvailableCategoriesForUser(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateCategoryRequest request) {
        var domain = mapper.toDomain(request, userId);
        var created = categoryUseCase.createCategory(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryUseCase.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}