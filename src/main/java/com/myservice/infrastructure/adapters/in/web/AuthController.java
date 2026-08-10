package com.myservice.infrastructure.adapters.in.web;

import com.myservice.domain.ports.in.AuthUseCase;
import com.myservice.infrastructure.adapters.in.web.dto.request.LoginRequest;
import com.myservice.infrastructure.adapters.in.web.dto.request.RefreshTokenRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authUseCase.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse response = authUseCase.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }
}
