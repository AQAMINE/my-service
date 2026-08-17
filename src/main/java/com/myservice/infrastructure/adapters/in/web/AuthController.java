package com.myservice.infrastructure.adapters.in.web;

import com.myservice.domain.model.AuthTokens;
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
        AuthTokens tokens = authUseCase.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(toAuthResponse(tokens));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthTokens tokens = authUseCase.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(toAuthResponse(tokens));
    }

    private AuthResponse toAuthResponse(AuthTokens tokens) {
        if (tokens == null) {
            return null;
        }
        return new AuthResponse(
                tokens.getAccessToken(),
                tokens.getExpiresIn(),
                tokens.getRefreshExpiresIn(),
                tokens.getRefreshToken(),
                tokens.getTokenType()
        );
    }
}
