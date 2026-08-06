package com.myservice.auth.service;

import com.myservice.auth.dto.request.LoginRequest;
import com.myservice.auth.dto.request.RefreshTokenRequest;
import com.myservice.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}