package com.myservice.domain.ports.out;

import com.myservice.domain.model.AccountCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountCategoryRepositoryPort {
    AccountCategory save(AccountCategory category);
    Optional<AccountCategory> findById(UUID id);
    List<AccountCategory> findAllAvailableForUser(UUID userId);
    void deleteById(UUID id);
}
