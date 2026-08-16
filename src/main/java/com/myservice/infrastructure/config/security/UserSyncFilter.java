package com.myservice.infrastructure.config.security;

import com.myservice.domain.ports.out.UserRepositoryPort;
import com.myservice.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserRepositoryPort userRepositoryPort;
    
    // Cache en mémoire très léger pour éviter de requêter PostgreSQL à chaque appel API
    private final Set<UUID> syncedUserCache = ConcurrentHashMap.newKeySet();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            UUID userId = UUID.fromString(jwt.getSubject());

            if (!syncedUserCache.contains(userId)) {
                if (!userRepositoryPort.existsById(userId)) {
                    User newUser = User.builder()
                            .id(userId)
                            .email(jwt.getClaimAsString("email"))
                            .firstName(jwt.getClaimAsString("given_name"))
                            .lastName(jwt.getClaimAsString("family_name"))
                            .preferences(Map.of())
                            .isActive(true)
                            .build();

                    userRepositoryPort.save(newUser);
                }
                // Ajout dans le cache
                syncedUserCache.add(userId);
            }
        }

        filterChain.doFilter(request, response);
    }
}