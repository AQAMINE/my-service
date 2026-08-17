package com.myservice.infrastructure.adapters.in.web.controller;

import com.myservice.application.ports.in.ExternalAccountUseCase;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateExternalAccountRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.ExternalAccountResponse;
import com.myservice.infrastructure.adapters.in.web.mapper.ExternalAccountWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myservice.infrastructure.adapters.in.web.dto.response.AccountPasswordResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class ExternalAccountController {

    private final ExternalAccountUseCase accountUseCase;
    private final ExternalAccountWebMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExternalAccountResponse>> getUserAccounts(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID providerId) {

        List<ExternalAccountResponse> accounts;

        if (categoryId != null) {
            accounts = accountUseCase.getAccountsByUserIdAndCategory(userId, categoryId)
                    .stream().map(mapper::toResponse).toList();
        } else if (providerId != null) {
            accounts = accountUseCase.getAccountsByUserIdAndProvider(userId, providerId)
                    .stream().map(mapper::toResponse).toList();
        } else {
            accounts = accountUseCase.getAccountsByUserId(userId)
                    .stream().map(mapper::toResponse).toList();
        }

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}/password")
    public ResponseEntity<AccountPasswordResponse> getAccountPassword(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {

        String rawPassword = accountUseCase.revealPassword(id, userId);
        return ResponseEntity.ok(new AccountPasswordResponse(rawPassword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalAccountResponse> getAccountById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {

        var account = accountUseCase.getAccountByIdAndUserId(id, userId);
        return ResponseEntity.ok(mapper.toResponse(account));
    }

    @PostMapping
    public ResponseEntity<ExternalAccountResponse> createAccount(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateExternalAccountRequest request) {
        var domain = mapper.toDomain(request, userId);
        var created = accountUseCase.createAccount(domain, request.rawPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountUseCase.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}