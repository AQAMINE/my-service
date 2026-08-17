package com.myservice.infrastructure.adapters.out.persistence.adapter;

import com.myservice.domain.model.User;
import com.myservice.domain.ports.out.UserRepositoryPort;
import com.myservice.infrastructure.adapters.out.persistence.mapper.UserPersistenceMapper;
import com.myservice.infrastructure.adapters.out.persistence.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository userRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        var savedEntity = userRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }
}