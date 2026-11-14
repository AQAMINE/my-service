package com.myservice.infrastructure.adapters.in.web.controller;

import com.myservice.application.service.UserManagementService;
import com.myservice.infrastructure.adapters.in.web.dto.request.CreateUserRequest;
import com.myservice.infrastructure.config.security.IsAdmin;
import com.myservice.infrastructure.adapters.in.web.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Gestion des Utilisateurs", description = "Endpoints pour l'administration des comptes utilisateur Keycloak et PostgreSQL")
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur", description = "Ajoute un utilisateur dans Keycloak et invalide le cache Redis.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données de la requête invalides (validation DTO)"),
        @ApiResponse(responseCode = "409", description = "Nom d'utilisateur ou email déjà existant")
    })
    @IsAdmin
    public ResponseEntity<Void> registerUser(@Valid @RequestBody CreateUserRequest request) {
        userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(
        summary = "Récupérer la liste des utilisateurs",
        description = "Retourne tous les utilisateurs enregistrés dans Keycloak. Les résultats sont mis en cache dans Redis pour accélérer les temps de réponse."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
        @ApiResponse(responseCode = "401", description = "Jeton JWT manquant ou invalide", content = @Content),
        @ApiResponse(responseCode = "403", description = "Droits insuffisants pour accéder à cette ressource", content = @Content)
    })
    @IsAdmin
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activer ou désactiver un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @IsAdmin
    public ResponseEntity<Void> toggleUserStatus(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {

        userManagementService.toggleUserStatus(id, enabled);
        return ResponseEntity.noContent().build();
    }
}