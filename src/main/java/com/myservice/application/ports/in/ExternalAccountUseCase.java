package com.myservice.application.ports.in;

import com.myservice.domain.model.ExternalAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExternalAccountUseCase {
    ExternalAccount createAccount(ExternalAccount account, String rawPassword);

    List<ExternalAccount> getAccountsByUserId(UUID userId);
    List<ExternalAccount> getAccountsByUserIdAndCategory(UUID userId, UUID categoryId);
    List<ExternalAccount> getAccountsByUserIdAndProvider(UUID userId, UUID providerId);

    ExternalAccount getAccountByIdAndUserId(UUID accountId, UUID userId);
    
    Optional<ExternalAccount> getAccountById(UUID id);
    String revealPassword(UUID accountId, UUID userId);
    void deleteAccount(UUID id);
}