package com.myservice.application.service;

import com.myservice.application.ports.in.AccountCategoryUseCase;
import com.myservice.domain.model.AccountCategory;
import com.myservice.domain.ports.out.AccountCategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountCategoryService implements AccountCategoryUseCase {

    private final AccountCategoryRepositoryPort categoryRepositoryPort;

    @Override
    public AccountCategory createCategory(AccountCategory category) {
        return categoryRepositoryPort.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountCategory> getAvailableCategoriesForUser(UUID userId) {
        return categoryRepositoryPort.findAllAvailableForUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountCategory> getCategoryById(UUID id) {
        return categoryRepositoryPort.findById(id);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepositoryPort.deleteById(id);
    }
}