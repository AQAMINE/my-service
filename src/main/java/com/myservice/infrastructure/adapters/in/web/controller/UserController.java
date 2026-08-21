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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping
    @IsAdmin
    public ResponseEntity<Void> registerUser(@Valid @RequestBody CreateUserRequest request) {
        userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @IsAdmin
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/status")
    @IsAdmin
    public ResponseEntity<Void> toggleUserStatus(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {

        userManagementService.toggleUserStatus(id, enabled);
        return ResponseEntity.noContent().build();
    }
}