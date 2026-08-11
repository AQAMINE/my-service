package com.myservice.application.ports.in;

import com.myservice.domain.model.AccountCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountCategoryUseCase {
    AccountCategory createCategory(AccountCategory category);
    List<AccountCategory> getAvailableCategoriesForUser(UUID userId);
    Optional<AccountCategory> getCategoryById(UUID id);
    void deleteCategory(UUID id);
}