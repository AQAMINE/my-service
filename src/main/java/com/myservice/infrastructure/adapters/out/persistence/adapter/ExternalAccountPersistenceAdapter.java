package com.myservice.infrastructure.adapters.out.persistence.adapter;

import com.myservice.domain.model.ExternalAccount;
import com.myservice.domain.ports.out.ExternalAccountRepositoryPort;
import com.myservice.infrastructure.adapters.out.persistence.mapper.ExternalAccountPersistenceMapper;
import com.myservice.infrastructure.adapters.out.persistence.repository.SpringDataExternalAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExternalAccountPersistenceAdapter implements ExternalAccountRepositoryPort {

    private final SpringDataExternalAccountRepository accountRepository;
    private final ExternalAccountPersistenceMapper mapper;

    @Override
    public ExternalAccount save(ExternalAccount account) {
        var entity = mapper.toEntity(account);
        var savedEntity = accountRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ExternalAccount> findById(UUID id) {
        return accountRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ExternalAccount> findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ExternalAccount> findByUserIdAndCategoryId(UUID userId, UUID categoryId) {
        return accountRepository.findByUserIdAndCategoryId(userId, categoryId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ExternalAccount> findByUserIdAndProviderId(UUID userId, UUID providerId) {
        return accountRepository.findByUserIdAndProviderId(userId, providerId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }
}