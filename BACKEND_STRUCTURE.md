# my-service — Backend Structure

Spring Boot backend (`com.my.service` / `com.myservice`), Java 21, Maven.
Auth via Keycloak (OAuth2 resource server). Schema managed with Flyway (PostgreSQL).
Persistence via Spring Data JPA. Hexagonal architecture (ports & adapters).

```
my-service/
├── .dockerignore
├── .gitattributes
├── .gitignore
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── .vscode/
│   └── settings.json
├── BACKEND_STRUCTURE.md
├── Dockerfile
├── HELP.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── myservice/
    │   │           ├── MyServiceApplication.java
    │   │           │
    │   │           ├── domain/                         # Cœur Métier Pure
    │   │           │   ├── exception/                  # Exceptions métier
    │   │           │   ├── model/                      # Entités / modèles du domaine
    │   │           │   │   ├── AccountCategory.java
    │   │           │   │   ├── AuthTokens.java
    │   │           │   │   ├── ExternalAccount.java
    │   │           │   │   ├── Provider.java
    │   │           │   │   └── User.java
    │   │           │   └── ports/                      # Contrats / Interfaces du domaine
    │   │           │       ├── in/                     # Use Cases (Cas d'utilisation)
    │   │           │       │   └── AuthUseCase.java
    │   │           │       └── out/                    # Ports sortants (repositories, gateways)
    │   │           │           ├── AccountCategoryRepositoryPort.java
    │   │           │           ├── EncryptionServicePort.java
    │   │           │           ├── ExternalAccountRepositoryPort.java
    │   │           │           ├── ProviderRepositoryPort.java
    │   │           │           └── UserRepositoryPort.java
    │   │           │
    │   │           ├── application/                    # Implémentations / Services Métier
    │   │           │   └── service/
    │   │           │       └── KeycloakAuthServiceImpl.java
    │   │           │
    │   │           └── infrastructure/                 # Adaptateurs & Framework (Spring/Keycloak)
    │   │               ├── adapters/
    │   │               │   ├── in/
    │   │               │   │   └── web/                # Contrôleurs REST & DTOs
    │   │               │   │       ├── AuthController.java
    │   │               │   │       ├── TestController.java
    │   │               │   │       └── dto/
    │   │               │   │           ├── request/
    │   │               │   │           │   ├── LoginRequest.java
    │   │               │   │           │   └── RefreshTokenRequest.java
    │   │               │   │           └── response/
    │   │               │   │               └── AuthResponse.java
    │   │               │   │
    │   │               │   └── out/
    │   │               │       └── persistence/         # Adaptateur JPA / PostgreSQL
    │   │               │           ├── entity/
    │   │               │           │   ├── AccountCategoryEntity.java
    │   │               │           │   ├── ExternalAccountEntity.java
    │   │               │           │   ├── ProviderEntity.java
    │   │               │           │   └── UserEntity.java
    │   │               │           └── repository/
    │   │               │               ├── SpringDataCategoryRepository.java
    │   │               │               ├── SpringDataExternalAccountRepository.java
    │   │               │               ├── SpringDataProviderRepository.java
    │   │               │               └── SpringDataUserRepository.java
    │   │               │
    │   │               └── config/                     # Configurations Spring Security
    │   │                   ├── SecurityConfig.java
    │   │                   └── KeycloakJwtAuthenticationConverter.java
    │   │
    │   └── resources/
    │       ├── application.yml
    │       ├── db/
    │       │   └── migration/                      # Migrations Flyway SQL
    │       │       ├── V1__init_schema.sql
    │       │       └── V2__insert_default_data.sql
    │       ├── static/
    │       └── templates/
    └── test/
        └── java/
            └── com/
                └── myservice/
                    └── MyServiceApplicationTests.java
```

## Architecture layers

| Layer | Package | Purpose |
|-------|---------|---------|
| Domain | `domain.model`, `domain.exception`, `domain.ports.in`, `domain.ports.out` | Models, exceptions, inbound & outbound ports (no infra DTOs) |
| Application | `application.service` | Use-case implementations |
| Infrastructure (in) | `infrastructure.adapters.in.web` | REST controllers; map HTTP DTOs ↔ domain |
| Infrastructure (out) | `infrastructure.adapters.out.persistence` | JPA entities & Spring Data repos (DB adapters) |
| Infrastructure (config) | `infrastructure.config` | Spring Security / JWT wiring |

## File roles

### Entry & application

| File | Role |
|------|------|
| `MyServiceApplication.java` | Spring Boot entry point |
| `KeycloakAuthServiceImpl.java` | Implements `AuthUseCase`; calls Keycloak token endpoint and returns `AuthTokens` |

### Domain models

| File | Role |
|------|------|
| `User.java` | Domain user (Keycloak `sub` as id, profile, preferences) |
| `AccountCategory.java` | Domain account category (global if `userId` is null, else user-owned) |
| `Provider.java` | Domain provider / service brand (global or user-owned) |
| `ExternalAccount.java` | Domain external account credentials linked to user, category, and provider |
| `AuthTokens.java` | Domain auth tokens returned by login / refresh |

### Domain ports (in)

| File | Role |
|------|------|
| `AuthUseCase.java` | Inbound port for login / refresh (primitives in → `AuthTokens` out) |

### Domain ports (out)

| File | Role |
|------|------|
| `UserRepositoryPort.java` | Outbound port to persist / load users |
| `AccountCategoryRepositoryPort.java` | Outbound port for categories (incl. global + user-available) |
| `ProviderRepositoryPort.java` | Outbound port for providers (incl. global + user-available) |
| `ExternalAccountRepositoryPort.java` | Outbound port for external accounts (by user / category / provider) |
| `EncryptionServicePort.java` | Outbound port to encrypt / decrypt passwords (`EncryptionResult`) |

### Infrastructure — web (in)

| File | Role |
|------|------|
| `AuthController.java` | REST `/api/auth`; maps `LoginRequest` / `RefreshTokenRequest` ↔ `AuthUseCase` / `AuthResponse` |
| `TestController.java` | Public / private hello endpoints for security smoke tests |
| `LoginRequest.java` | HTTP body for login |
| `RefreshTokenRequest.java` | HTTP body for token refresh |
| `AuthResponse.java` | HTTP JSON token response (`access_token`, `refresh_token`, …) |

### Infrastructure — persistence (out)

| File | Role |
|------|------|
| `UserEntity.java` | JPA mapping for table `users` |
| `AccountCategoryEntity.java` | JPA mapping for table `account_categories` |
| `ProviderEntity.java` | JPA mapping for table `providers` |
| `ExternalAccountEntity.java` | JPA mapping for table `external_accounts` |
| `SpringDataUserRepository.java` | Spring Data JPA repo for `UserEntity` (`findByEmail`) |
| `SpringDataCategoryRepository.java` | Spring Data JPA repo for categories (`findAllAvailableForUser`) |
| `SpringDataProviderRepository.java` | Spring Data JPA repo for providers (`findAllAvailableForUser`) |
| `SpringDataExternalAccountRepository.java` | Spring Data JPA repo for external accounts (by user / category / provider) |

### Infrastructure — config

| File | Role |
|------|------|
| `SecurityConfig.java` | Security filter chain, public routes, `RestTemplate` bean |
| `KeycloakJwtAuthenticationConverter.java` | Maps Keycloak JWT realm roles to Spring authorities |

### Resources

| File | Role |
|------|------|
| `application.yml` | Server, datasource, JPA, Flyway, OAuth2 issuer, Keycloak client |
| `V1__init_schema.sql` | Creates PostgreSQL schema (users, categories, providers, external_accounts) |
| `V2__insert_default_data.sql` | Seeds default / system data |
| `MyServiceApplicationTests.java` | Spring Boot context load test |

## Package overview

| Path | Role |
|------|------|
| `com.myservice` | Application entry point |
| `com.myservice.domain.model` | Pure domain models |
| `com.myservice.domain.exception` | Domain / business exceptions (folder ready) |
| `com.myservice.domain.ports.in` | Inbound use-case ports |
| `com.myservice.domain.ports.out` | Outbound repository & encryption ports |
| `com.myservice.application.service` | Use-case implementations |
| `com.myservice.infrastructure.adapters.in.web` | REST controllers |
| `com.myservice.infrastructure.adapters.in.web.dto.*` | HTTP request / response DTOs |
| `com.myservice.infrastructure.adapters.out.persistence.entity` | JPA entities |
| `com.myservice.infrastructure.adapters.out.persistence.repository` | Spring Data JPA repositories |
| `com.myservice.infrastructure.config` | Spring Security & JWT config |
| `src/main/resources/db/migration` | Flyway SQL migrations |
| `src/test/java` | Tests |

## Root files

| File | Description |
|------|-------------|
| `pom.xml` | Maven deps (Web, OAuth2, JPA, PostgreSQL, Flyway, Lombok) |
| `Dockerfile` | Multi-stage build (Temurin 21) |
| `mvnw` / `mvnw.cmd` | Maven Wrapper scripts |
| `.dockerignore` | Files excluded from Docker build context |
| `BACKEND_STRUCTURE.md` | This structure reference |
