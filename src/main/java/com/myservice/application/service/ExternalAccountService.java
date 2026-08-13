package com.myservice.application.service;

import com.myservice.application.ports.in.ExternalAccountUseCase;
import com.myservice.domain.model.ExternalAccount;
import com.myservice.domain.ports.out.EncryptionServicePort;
import com.myservice.domain.ports.out.ExternalAccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalAccountService implements ExternalAccountUseCase {

    private final ExternalAccountRepositoryPort externalAccountRepositoryPort;
    private final EncryptionServicePort encryptionServicePort; // <-- Injecté ici !

    @Override
    public ExternalAccount createAccount(ExternalAccount account, String rawPassword) {
        if (rawPassword != null && !rawPassword.isBlank()) {
            // Appel au microservice Python gRPC via l'adaptateur
            var encryptionResult = encryptionServicePort.encrypt(rawPassword);
            
            account.setEncryptedPassword(encryptionResult.cipherTextBase64());
            account.setEncryptionIv(encryptionResult.ivBase64());
        }

        return externalAccountRepositoryPort.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalAccount> getAccountsByUserId(UUID userId) {
        return externalAccountRepositoryPort.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalAccount> getAccountsByUserIdAndCategory(UUID userId, UUID categoryId) {
        return externalAccountRepositoryPort.findByUserIdAndCategoryId(userId, categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalAccount> getAccountsByUserIdAndProvider(UUID userId, UUID providerId) {
        return externalAccountRepositoryPort.findByUserIdAndProviderId(userId, providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExternalAccount> getAccountById(UUID id) {
        return externalAccountRepositoryPort.findById(id);
    }

    @Override
    public void deleteAccount(UUID id) {
        externalAccountRepositoryPort.deleteById(id);
    }
}