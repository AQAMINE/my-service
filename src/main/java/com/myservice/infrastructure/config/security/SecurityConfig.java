package com.myservice.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Activer le support CORS configuré dans WebConfig
            .cors(Customizer.withDefaults())
            
            // 2. Désactiver CSRF pour API REST Stateless
            .csrf(csrf -> csrf.disable())
            
            // 3. Mode Stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Règles d'accès HTTP
            .authorizeHttpRequests(auth -> auth
                // Autoriser les requêtes OPTIONS (Preflight CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Endpoints publics & d'authentification Keycloak
                .requestMatchers("/api/public/**", "/api/auth/**").permitAll()
                
                // Documentation Swagger / OpenApi / Actuator (optionnel)
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/actuator/**").permitAll()
                
                // Tout le reste exige une authentification
                .anyRequest().authenticated()
            )
            
            // 5. Integration Keycloak Resource Server (avec ton converter)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
            );

        return http.build();
    }

    // Bean requis pour KeycloakAuthServiceImpl
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
