# my-service — Backend Structure

Spring Boot backend (`com.my.service` / `com.myservice`), Java 21, Maven.
Auth via Keycloak (OAuth2 resource server). Schema managed with Flyway (PostgreSQL).
Persistence via Spring Data JPA. Hexagonal architecture (ports & adapters).

## ⚙️ Environment & Local Setup

| Item | Value |
|------|-------|
| Application Port | `8081` |
| Java SDK | `21` |
| PostgreSQL Container | `my-service-db` |
| Host → Container Port | `5433` → `5432` |
| Database Name | `myservicedb` |
| Keycloak Realm | `my-service-realm` |
| Issuer URI | `http://localhost:8080/realms/my-service-realm` |
| Keycloak Client | `my-service-backend` |

## 🗄️ Database & Flyway Schema

Migrations live under `src/main/resources/db/migration/`:

- `V1__init_schema.sql` — creates the PostgreSQL schema
- `V2__insert_default_data.sql` — inserts system seed data

### Tables

| Table | Description |
|-------|-------------|
| `users` | Synchronized JIT via Keycloak (`id` UUID = Keycloak `sub`) |
| `account_categories` | Global categories (`user_id IS NULL`) and user-owned ones. Unique constraint on `(user_id, slug)` |
| `providers` | Global and custom providers. UI fields: `color`, `website_url`, `logo_url`. Unique constraint on `(user_id, slug)` |
| `external_accounts` | Stored credentials (`encrypted_password` + `encryption_iv`) linked to user, category, and provider |

### Seed data (system defaults, `user_id IS NULL`)

**Categories**

| Name | Slug |
|------|------|
| Social Media | `social_media` |
| Email Service | `email_service` |
| Hosting & Cloud | `hosting_cloud` |
| Payment Provider | `payment_provider` |

**Providers**

| Name | Slug |
|------|------|
| Instagram | `instagram` |
| Facebook | `facebook` |
| Hostinger | `hostinger` |
| Fiverr | `fiverr` |

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
    │   │           ├── application/                    # Cas d'utilisation & services
    │   │           │   ├── ports/
    │   │           │   │   └── in/                     # Use cases applicatifs
    │   │           │   │       ├── AccountCategoryUseCase.java
    │   │           │   │       ├── ExternalAccountUseCase.java
    │   │           │   │       └── ProviderUseCase.java
    │   │           │   └── service/
    │   │           │       ├── AccountCategoryService.java
    │   │           │       ├── ExternalAccountService.java
    │   │           │       ├── KeycloakAuthServiceImpl.java
    │   │           │       └── ProviderService.java
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
    │   │               │           ├── adapter/
    │   │               │           │   ├── AccountCategoryPersistenceAdapter.java
    │   │               │           │   ├── ExternalAccountPersistenceAdapter.java
    │   │               │           │   ├── ProviderPersistenceAdapter.java
    │   │               │           │   └── UserPersistenceAdapter.java
    │   │               │           ├── entity/
    │   │               │           │   ├── AccountCategoryEntity.java
    │   │               │           │   ├── ExternalAccountEntity.java
    │   │               │           │   ├── ProviderEntity.java
    │   │               │           │   └── UserEntity.java
    │   │               │           ├── mapper/
    │   │               │           │   ├── AccountCategoryPersistenceMapper.java
    │   │               │           │   ├── ExternalAccountPersistenceMapper.java
    │   │               │           │   ├── ProviderPersistenceMapper.java
    │   │               │           │   └── UserPersistenceMapper.java
    │   │               │           └── repository/
    │   │               │               ├── SpringDataCategoryRepository.java
    │   │               │               ├── SpringDataExternalAccountRepository.java
    │   │               │               ├── SpringDataProviderRepository.java
    │   │               │               └── SpringDataUserRepository.java
    │   │               │
    │   │               └── config/                     # Configurations Spring
    │   │                   ├── JacksonConfig.java
    │   │                   ├── KeycloakJwtAuthenticationConverter.java
    │   │                   └── SecurityConfig.java
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
| Application | `application.ports.in`, `application.service` | Application use cases & service implementations |
| Infrastructure (in) | `infrastructure.adapters.in.web` | REST controllers; map HTTP DTOs ↔ domain |
| Infrastructure (out) | `infrastructure.adapters.out.persistence` | Persistence adapters, mappers, JPA entities & Spring Data repos |
| Infrastructure (config) | `infrastructure.config` | Spring Security / JWT / Jackson wiring |

## File roles

### Entry & application

| File | Role |
|------|------|
| `MyServiceApplication.java` | Spring Boot entry point |
| `AccountCategoryUseCase.java` | Application port: create / list / get / delete categories |
| `ProviderUseCase.java` | Application port: create / list / get / delete providers |
| `ExternalAccountUseCase.java` | Application port: create (with raw password) / list / get / delete external accounts |
| `AccountCategoryService.java` | Implements `AccountCategoryUseCase` via `AccountCategoryRepositoryPort` |
| `ProviderService.java` | Implements `ProviderUseCase` via `ProviderRepositoryPort` |
| `ExternalAccountService.java` | Implements `ExternalAccountUseCase` via `ExternalAccountRepositoryPort` (gRPC encrypt TODO) |
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
| `EncryptionServicePort.java` | Outbound port for password encrypt / decrypt; delegates to an external Python microservice via **gRPC** (AES-GCM with a unique IV per entry) |

### Infrastructure — web (in)

| File | Role |
|------|------|
| `AuthController.java` | REST `/api/auth`; maps `LoginRequest` / `RefreshTokenRequest` ↔ `AuthUseCase` / `AuthResponse` |
| `TestController.java` | Public / private hello endpoints for security smoke tests |
| `LoginRequest.java` | HTTP body for login |
| `RefreshTokenRequest.java` | HTTP body for token refresh |
| `AuthResponse.java` | HTTP JSON token response (`access_token`, `refresh_token`, …) |

### Infrastructure — persistence adapters (out)

| File | Role |
|------|------|
| `UserPersistenceAdapter.java` | Implements `UserRepositoryPort` via Spring Data + `UserPersistenceMapper` |
| `AccountCategoryPersistenceAdapter.java` | Implements `AccountCategoryRepositoryPort` via Spring Data + mapper |
| `ProviderPersistenceAdapter.java` | Implements `ProviderRepositoryPort` via Spring Data + mapper |
| `ExternalAccountPersistenceAdapter.java` | Implements `ExternalAccountRepositoryPort` via Spring Data + mapper |

### Infrastructure — persistence mappers (out)

| File | Role |
|------|------|
| `UserPersistenceMapper.java` | Maps `User` ↔ `UserEntity` (incl. preferences `Map` ↔ JSON string) |
| `AccountCategoryPersistenceMapper.java` | Maps `AccountCategory` ↔ `AccountCategoryEntity` |
| `ProviderPersistenceMapper.java` | Maps `Provider` ↔ `ProviderEntity` |
| `ExternalAccountPersistenceMapper.java` | Maps `ExternalAccount` ↔ `ExternalAccountEntity` |

### Infrastructure — persistence entities & repos (out)

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
| `JacksonConfig.java` | Primary `ObjectMapper` (JavaTimeModule, no date timestamps, ignore unknown props) |

### Resources

| File | Role |
|------|------|
| `application.yml` | Server (`8081`), datasource, JPA, Flyway, OAuth2 issuer, Keycloak client |
| `V1__init_schema.sql` | Creates PostgreSQL schema (users, categories, providers, external_accounts) |
| `V2__insert_default_data.sql` | Seeds system categories and providers |
| `MyServiceApplicationTests.java` | Spring Boot context load test |

## Package overview

| Path | Role |
|------|------|
| `com.myservice` | Application entry point |
| `com.myservice.domain.model` | Pure domain models |
| `com.myservice.domain.exception` | Domain / business exceptions (folder ready) |
| `com.myservice.domain.ports.in` | Domain inbound ports (`AuthUseCase`) |
| `com.myservice.domain.ports.out` | Outbound repository & encryption ports (incl. gRPC encryption) |
| `com.myservice.application.ports.in` | Application use-case ports (categories, providers, external accounts) |
| `com.myservice.application.service` | Use-case / auth service implementations |
| `com.myservice.infrastructure.adapters.in.web` | REST controllers |
| `com.myservice.infrastructure.adapters.in.web.dto.*` | HTTP request / response DTOs |
| `com.myservice.infrastructure.adapters.out.persistence.adapter` | Port implementations (hexagonal outbound adapters) |
| `com.myservice.infrastructure.adapters.out.persistence.mapper` | Domain ↔ JPA entity mappers |
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
