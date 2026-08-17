package com.myservice.domain.ports.out;

import com.myservice.domain.model.Provider;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ProviderRepositoryPort {
    Provider save(Provider provider);
    Optional<Provider> findById(UUID id);
    List<Provider> findAllAvailableForUser(UUID userId);
    void deleteById(UUID id);
}
