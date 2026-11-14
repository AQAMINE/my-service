package com.myservice.infrastructure.adapters.in.web.controller;

import com.myservice.domain.model.AuthTokens;
import com.myservice.domain.ports.in.AuthUseCase;
import com.myservice.infrastructure.adapters.in.web.dto.request.LoginRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.AccessTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        AuthTokens tokens = authUseCase.login(loginRequest.getUsername(), loginRequest.getPassword());
        setRefreshTokenCookie(response, tokens.getRefreshToken(), tokens.getRefreshExpiresIn());

        return ResponseEntity.ok(toAccessTokenResponse(tokens));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        AuthTokens tokens = authUseCase.refreshToken(refreshToken);
        setRefreshTokenCookie(response, tokens.getRefreshToken(), tokens.getRefreshExpiresIn());

        return ResponseEntity.ok(toAccessTokenResponse(tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Supprime le cookie côté navigateur en lui donnant une durée de vie nulle
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // Mettre à true en production (HTTPS)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.noContent().build();
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, long refreshExpiresIn) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Mettre à true en production (HTTPS)
                .path("/api/auth") // Le cookie ne sera envoyé QUE pour les requêtes vers /api/auth (refresh/logout)
                .maxAge(refreshExpiresIn)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private AccessTokenResponse toAccessTokenResponse(AuthTokens tokens) {
        if (tokens == null) {
            return null;
        }
        return new AccessTokenResponse(
                tokens.getAccessToken(),
                tokens.getExpiresIn(),
                tokens.getTokenType()
        );
    }
}