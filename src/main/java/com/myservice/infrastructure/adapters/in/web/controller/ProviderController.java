package com.myservice.infrastructure.adapters.in.web.controller;

import com.myservice.application.ports.in.ProviderUseCase;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateProviderRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.ProviderResponse;
import com.myservice.infrastructure.adapters.in.web.mapper.ProviderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderUseCase providerUseCase;
    private final ProviderWebMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProviderResponse>> getAvailableProviders(
            @RequestHeader("X-User-Id") UUID userId) {
        var providers = providerUseCase.getAvailableProvidersForUser(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(providers);
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> createProvider(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateProviderRequest request) {
        var domain = mapper.toDomain(request, userId);
        var created = providerUseCase.createProvider(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable UUID id) {
        providerUseCase.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }
}