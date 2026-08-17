package com.myservice.application.ports.in;

import com.myservice.domain.model.Provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderUseCase {
    Provider createProvider(Provider provider);
    List<Provider> getAvailableProvidersForUser(UUID userId);
    Optional<Provider> getProviderById(UUID id);
    void deleteProvider(UUID id);
}