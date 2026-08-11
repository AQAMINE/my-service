package com.myservice.domain.ports.out;

import com.myservice.domain.model.ExternalAccount;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ExternalAccountRepositoryPort {
    ExternalAccount save(ExternalAccount account);
    Optional<ExternalAccount> findById(UUID id);
    List<ExternalAccount> findByUserId(UUID userId);
    List<ExternalAccount> findByUserIdAndCategoryId(UUID userId, UUID categoryId);
    List<ExternalAccount> findByUserIdAndProviderId(UUID userId, UUID providerId);
    void deleteById(UUID id);
}
