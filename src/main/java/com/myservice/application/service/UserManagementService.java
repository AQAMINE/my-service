package com.myservice.application.service;

import com.myservice.infrastructure.adapters.in.web.dto.request.CreateUserRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.UserResponse;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.myservice.domain.ports.out.UserRepositoryPort;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    public void createUser(CreateUserRequest request) {
        // 1. Définir le mot de passe
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(request.password());

        // 2. Définir les informations de l'utilisateur
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setCredentials(List.of(passwordCred));

        // 3. Appel géré par le SDK Keycloak Admin
        Response response = keycloakAdmin.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Erreur de création Keycloak: " + response.getStatus());
        }
    }

    public List<UserResponse> getAllUsers() {
        // 1. Récupérer la liste des représentations d'utilisateurs depuis Keycloak
        List<UserRepresentation> users = keycloakAdmin.realm(realm).users().list();

        // 2. Transformer en DTO de réponse
        return users.stream()
                .map(user -> new UserResponse(
                        UUID.fromString(user.getId()), // Le sub Keycloak est l'UUID
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.isEnabled()))
                .toList();
    }

    // Injection du UserRepositoryPort dans UserManagementService
    private final UserRepositoryPort userRepositoryPort;

    public void toggleUserStatus(UUID userId, boolean enabled) {
        String userIdStr = userId.toString();

        // 1. Mise à jour dans Keycloak
        UserRepresentation user = keycloakAdmin.realm(realm).users().get(userIdStr).toRepresentation();
        user.setEnabled(enabled);
        keycloakAdmin.realm(realm).users().get(userIdStr).update(user);

        // 2. Mise à jour facultative mais recommandée dans PostgreSQL (si ton entité
        // User a un champ isActive)
        userRepositoryPort.findById(userId).ifPresent(domainUser -> {
            domainUser.setIsActive(enabled);
            userRepositoryPort.save(domainUser);
        });
    }
}