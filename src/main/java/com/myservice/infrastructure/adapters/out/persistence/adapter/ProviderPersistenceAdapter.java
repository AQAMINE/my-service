package com.myservice.infrastructure.adapters.out.persistence.adapter;

import com.myservice.domain.model.Provider;
import com.myservice.domain.ports.out.ProviderRepositoryPort;
import com.myservice.infrastructure.adapters.out.persistence.mapper.ProviderPersistenceMapper;
import com.myservice.infrastructure.adapters.out.persistence.repository.SpringDataProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProviderPersistenceAdapter implements ProviderRepositoryPort {

    private final SpringDataProviderRepository providerRepository;
    private final ProviderPersistenceMapper mapper;

    @Override
    public Provider save(Provider provider) {
        var entity = mapper.toEntity(provider);
        var savedEntity = providerRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Provider> findById(UUID id) {
        return providerRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Provider> findAllAvailableForUser(UUID userId) {
        return providerRepository.findAllAvailableForUser(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        providerRepository.deleteById(id);
    }
}