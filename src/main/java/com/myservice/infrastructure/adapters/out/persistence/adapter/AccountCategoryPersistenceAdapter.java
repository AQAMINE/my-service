package com.myservice.infrastructure.adapters.out.persistence.adapter;

import com.myservice.domain.model.AccountCategory;
import com.myservice.domain.ports.out.AccountCategoryRepositoryPort;
import com.myservice.infrastructure.adapters.out.persistence.mapper.AccountCategoryPersistenceMapper;
import com.myservice.infrastructure.adapters.out.persistence.repository.SpringDataCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountCategoryPersistenceAdapter implements AccountCategoryRepositoryPort {

    private final SpringDataCategoryRepository categoryRepository;
    private final AccountCategoryPersistenceMapper mapper;

    @Override
    public AccountCategory save(AccountCategory category) {
        var entity = mapper.toEntity(category);
        var savedEntity = categoryRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AccountCategory> findById(UUID id) {
        return categoryRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AccountCategory> findAllAvailableForUser(UUID userId) {
        return categoryRepository.findAllAvailableForUser(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }
}