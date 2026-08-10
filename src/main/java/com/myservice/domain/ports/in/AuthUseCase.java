package com.myservice.domain.ports.in;

import com.myservice.infrastructure.adapters.in.web.dto.request.LoginRequest;
import com.myservice.infrastructure.adapters.in.web.dto.request.RefreshTokenRequest;
import com.myservice.infrastructure.adapters.in.web.dto.response.AuthResponse;

public interface AuthUseCase {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
