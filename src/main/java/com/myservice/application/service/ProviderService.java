package com.myservice.application.service;

import com.myservice.application.ports.in.ProviderUseCase;
import com.myservice.domain.model.Provider;
import com.myservice.domain.ports.out.ProviderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderService implements ProviderUseCase {

    private final ProviderRepositoryPort providerRepositoryPort;

    @Override
    public Provider createProvider(Provider provider) {
        return providerRepositoryPort.save(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Provider> getAvailableProvidersForUser(UUID userId) {
        return providerRepositoryPort.findAllAvailableForUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Provider> getProviderById(UUID id) {
        return providerRepositoryPort.findById(id);
    }

    @Override
    public void deleteProvider(UUID id) {
        providerRepositoryPort.deleteById(id);
    }
}