package com.myservice.domain.ports.in;

import com.myservice.domain.model.AuthTokens;

public interface AuthUseCase {
    AuthTokens login(String username, String password);
    AuthTokens refreshToken(String refreshToken);
}
