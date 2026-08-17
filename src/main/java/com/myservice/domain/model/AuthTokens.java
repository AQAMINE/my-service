package com.myservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthTokens {
    private String accessToken;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
}
