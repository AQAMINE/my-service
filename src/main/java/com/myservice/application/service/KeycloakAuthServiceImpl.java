package com.myservice.application.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myservice.domain.model.AuthTokens;
import com.myservice.domain.ports.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakAuthServiceImpl implements AuthUseCase {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public AuthTokens login(String username, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        return requestTokens(body);
    }

    @Override
    public AuthTokens refreshToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        return requestTokens(body);
    }

    private AuthTokens requestTokens(MultiValueMap<String, String> body) {
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<KeycloakTokenResponse> response =
                restTemplate.postForEntity(tokenUrl, request, KeycloakTokenResponse.class);

        return toAuthTokens(response.getBody());
    }

    private AuthTokens toAuthTokens(KeycloakTokenResponse response) {
        if (response == null) {
            return null;
        }
        return new AuthTokens(
                response.accessToken(),
                response.expiresIn(),
                response.refreshExpiresIn(),
                response.refreshToken(),
                response.tokenType()
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record KeycloakTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") Long expiresIn,
            @JsonProperty("refresh_expires_in") Long refreshExpiresIn,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("token_type") String tokenType
    ) {}
}
