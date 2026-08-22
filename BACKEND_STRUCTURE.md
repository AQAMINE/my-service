# my-service — Backend Structure

Spring Boot 4.1 backend (`com.my.service` / `com.myservice`), Java 21, Maven.
Auth via Keycloak (OAuth2 resource server). Authenticated users are JIT-synced into `users` by `UserSyncFilter`.
Persistence via Spring Data JPA. Hexagonal architecture (ports & adapters).
Password encryption via a Python gRPC microservice (`my-service-crypto`).

## Environment & local setup

| Item | Value |
|------|-------|
| Spring Boot | `4.1.0` |
| Application port | `8081` |
| Java SDK | `21` |
| PostgreSQL container | `my-service-db` |
| Host → container port | `5435` → `5432` |
| Database | `myservicedb` (schema `public`) |
| JDBC URL | `jdbc:postgresql://localhost:5435/myservicedb?currentSchema=public` |
| Keycloak (host) | `http://localhost:8085` |
| Keycloak realm | `my-service-realm` |
| Issuer URI | `http://localhost:8085/realms/my-service-realm` |
| Keycloak client | `my-service-backend` |
| Access token lifespan | `300s` (set in Keycloak: Realm settings → Tokens) |
| gRPC crypto service | `localhost:50051` (plaintext) |

Docker Compose maps the packaged backend to host **`8082`**. Local `./mvnw spring-boot:run` uses **`8081`** and `application.yml`.

## Database & Flyway

Flyway owns the schema. Hibernate `ddl-auto` is `none` (do not let JPA create/alter tables).

On **Spring Boot 4**, `flyway-core` alone does **not** run migrations. The project uses:

- `spring-boot-starter-flyway`
- `flyway-database-postgresql`

Migrations live under `src/main/resources/db/migration/`:

- `V1__init_schema.sql` — creates `public` schema + tables
- `V2__insert_default_data.sql` — system seed data (`user_id IS NULL`, explicit `gen_random_uuid()` ids)

### Tables

| Table | Description |
|-------|-------------|
| `users` | JIT-synced from JWT by `UserSyncFilter` (`id` UUID = Keycloak `sub`) |
| `account_categories` | Global (`user_id IS NULL`) and user-owned. Unique `(user_id, slug)` (`NULLS NOT DISTINCT`) |
| `providers` | Global and custom providers (`color`, `website_url`, `logo_url`). Unique `(user_id, slug)` |
| `external_accounts` | Credentials (`encrypted_password` + `encryption_iv`) linked to user, category, and provider |

### Seed data (system defaults)

**Categories (7)**

| Name | Slug |
|------|------|
| Social & Messaging | `social_messaging` |
| Hosting & Cloud | `hosting_cloud` |
| Developer & Tech | `developer_tech` |
| Freelance & Work | `freelance_work` |
| Payment & Banking | `payment_banking` |
| Entertainment & Streaming | `entertainment_streaming` |
| E-commerce & Shopping | `ecommerce_shopping` |

**Providers (41)** — grouped in V2: Social & Messaging, Developer & Tech, Hosting & Cloud, Freelance & Work, Payment & Banking, Entertainment & Streaming, E-commerce & Shopping.

## REST API

Public (no JWT):

| Method | Path |
|--------|------|
| `POST` | `/api/auth/login` |
| `POST` | `/api/auth/refresh` |
| `GET` | `/api/public/hello` |

Protected (JWT Bearer):

| Method | Path | Extra headers |
|--------|------|----------------|
| `GET` | `/api/private/hello` | — |
| `GET` / `POST` / `DELETE` | `/api/v1/categories` | `X-User-Id` = JWT `sub` (UUID) |
| `GET` / `POST` / `DELETE` | `/api/v1/providers` | `X-User-Id` = JWT `sub` (UUID) |
| `GET` / `POST` / `DELETE` | `/api/v1/accounts` | `X-User-Id` = JWT `sub` (UUID) |
| `GET` | `/api/v1/accounts/{id}` | `X-User-Id` = JWT `sub` (owner check) |
| `GET` | `/api/v1/accounts/{id}/password` | `X-User-Id` = JWT `sub`; decrypts via gRPC |

Missing JWT → **401**. Authenticated but missing/invalid `X-User-Id` → **400**.

First authenticated request for a new Keycloak user inserts a `users` row (JIT sync). Password reveal returns `{ "password": "..." }` and is owner-scoped.

Login body: `{ "username", "password" }`. Refresh body: `{ "refreshToken" }`.

## Tree

```
my-service/
├── BACKEND_STRUCTURE.md
├── Dockerfile
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/myservice/
    │   │   ├── MyServiceApplication.java
    │   │   ├── config/
    │   │   │   └── GrpcTestRunner.java
    │   │   ├── domain/
    │   │   │   ├── model/
    │   │   │   │   ├── AccountCategory.java
    │   │   │   │   ├── AuthTokens.java
    │   │   │   │   ├── ExternalAccount.java
    │   │   │   │   ├── Provider.java
    │   │   │   │   └── User.java
    │   │   │   └── ports/
    │   │   │       ├── in/
    │   │   │       │   └── AuthUseCase.java
    │   │   │       └── out/
    │   │   │           ├── AccountCategoryRepositoryPort.java
    │   │   │           ├── EncryptionServicePort.java
    │   │   │           ├── ExternalAccountRepositoryPort.java
    │   │   │           ├── ProviderRepositoryPort.java
    │   │   │           └── UserRepositoryPort.java
    │   │   ├── application/
    │   │   │   ├── ports/in/
    │   │   │   │   ├── AccountCategoryUseCase.java
    │   │   │   │   ├── ExternalAccountUseCase.java
    │   │   │   │   └── ProviderUseCase.java
    │   │   │   └── service/
    │   │   │       ├── AccountCategoryService.java
    │   │   │       ├── ExternalAccountService.java
    │   │   │       ├── KeycloakAuthServiceImpl.java
    │   │   │       └── ProviderService.java
    │   │   └── infrastructure/
    │   │       ├── adapters/
    │   │       │   ├── in/web/
    │   │       │   │   ├── TestController.java
    │   │       │   │   ├── controller/          # AuthController + /api/v1/*
    │   │       │   │   ├── dto/request|response/
    │   │       │   │   └── mapper/
    │   │       │   └── out/
    │   │       │       ├── grpc/
    │   │       │       │   └── EncryptionGrpcAdapter.java
    │   │       │       └── persistence/
    │   │       │           ├── adapter/
    │   │       │           ├── entity/
    │   │       │           ├── mapper/
    │   │       │           └── repository/
    │   │       └── config/
    │   │           ├── security/
    │   │           │   ├── IsAdmin.java
    │   │           │   ├── IsUserOrAdmin.java
    │   │           │   ├── KeycloakJwtAuthenticationConverter.java
    │   │           │   ├── SecurityConfig.java
    │   │           │   └── UserSyncFilter.java
    │   │           └── web/
    │   │               ├── JacksonConfig.java
    │   │               └── WebConfig.java
    │   ├── proto/
    │   │   └── crypto.proto
    │   └── resources/
    │       ├── application.yml
    │       └── db/migration/
    │           ├── V1__init_schema.sql
    │           └── V2__insert_default_data.sql
    └── test/java/com/myservice/
        └── MyServiceApplicationTests.java
```

Generated gRPC stubs from `crypto.proto` land in `com.myservice.infrastructure.adapters.out.grpc.proto` (protobuf-maven-plugin).

## Architecture layers

| Layer | Package | Purpose |
|-------|---------|---------|
| Domain | `domain.model`, `domain.ports.in`, `domain.ports.out` | Models and ports (no infra DTOs) |
| Application | `application.ports.in`, `application.service` | Use cases & service implementations |
| Infrastructure (in) | `infrastructure.adapters.in.web` | REST; HTTP DTOs ↔ domain |
| Infrastructure (out) | `infrastructure.adapters.out.persistence` | JPA adapters, mappers, entities, Spring Data |
| Infrastructure (out) | `infrastructure.adapters.out.grpc` | `EncryptionServicePort` via gRPC |
| Infrastructure (config) | `infrastructure.config` | Security / JWT / Jackson / CORS |

## File roles

### Entry & application

| File | Role |
|------|------|
| `MyServiceApplication.java` | Spring Boot entry point |
| `AccountCategoryUseCase.java` | Create / list / get / delete categories |
| `ProviderUseCase.java` | Create / list / get / delete providers |
| `ExternalAccountUseCase.java` | Create / list / get by id (owner) / reveal password / delete |
| `AccountCategoryService.java` | Implements category use case via `AccountCategoryRepositoryPort` |
| `ProviderService.java` | Implements provider use case via `ProviderRepositoryPort` |
| `ExternalAccountService.java` | Encrypts on create; decrypts on `revealPassword`; owner checks |
| `KeycloakAuthServiceImpl.java` | Implements `AuthUseCase`; Keycloak token endpoint → `AuthTokens` |
| `GrpcTestRunner.java` | Dev helper to ping the gRPC crypto client |

### Domain models

| File | Role |
|------|------|
| `User.java` | Domain user (Keycloak `sub` as id) |
| `AccountCategory.java` | Global if `userId` is null, else user-owned |
| `Provider.java` | Global or user-owned provider / brand |
| `ExternalAccount.java` | Credentials linked to user, category, provider |
| `AuthTokens.java` | Login / refresh tokens |

### Domain ports

| File | Role |
|------|------|
| `AuthUseCase.java` | Login / refresh |
| `UserRepositoryPort.java` | Persist / load users (`existsById` used by JIT sync) |
| `AccountCategoryRepositoryPort.java` | Categories (global + available for user) |
| `ProviderRepositoryPort.java` | Providers (global + available for user) |
| `ExternalAccountRepositoryPort.java` | Accounts by user / category / provider |
| `EncryptionServicePort.java` | Encrypt / decrypt; implemented by gRPC adapter (AES-GCM, unique IV) |

### Infrastructure — web (in)

| File | Role |
|------|------|
| `AuthController.java` | `/api/auth` login & refresh (`controller` package) |
| `TestController.java` | `/api/public/hello`, `/api/private/hello` |
| `CategoryController.java` | `/api/v1/categories` — requires `X-User-Id` |
| `ProviderController.java` | `/api/v1/providers` — requires `X-User-Id` |
| `ExternalAccountController.java` | `/api/v1/accounts` — list/create/delete; `GET /{id}`; `GET /{id}/password` |
| `LoginRequest.java` / `RefreshTokenRequest.java` | Auth HTTP bodies |
| `Create*Request.java` | Validated create bodies (`CreateExternalAccountRequest` includes `rawPassword`) |
| `AuthResponse.java` | JSON tokens (`access_token`, `refresh_token`, `expires_in`, …) |
| `AccountPasswordResponse.java` | `{ "password" }` from `GET /{id}/password` (plaintext after gRPC decrypt) |
| `*Response.java` | HTTP JSON (list/detail accounts never return secrets) |
| `*WebMapper.java` | Request / domain / response mapping |

### Infrastructure — persistence (out)

| File | Role |
|------|------|
| `*PersistenceAdapter.java` | Implements repository ports |
| `*PersistenceMapper.java` | Domain ↔ JPA (`User` preferences `Map` ↔ JSON) |
| `*Entity.java` | Tables `users`, `account_categories`, `providers`, `external_accounts` |
| `SpringData*Repository.java` | Spring Data JPA (`findAllAvailableForUser` for categories/providers) |

### Infrastructure — gRPC (out)

| File | Role |
|------|------|
| `crypto.proto` | `CryptoService` Encrypt / Decrypt contract |
| `EncryptionGrpcAdapter.java` | `@GrpcClient("crypto-service")` blocking stub |

### Infrastructure — config

| File | Role |
|------|------|
| `SecurityConfig.java` | Stateless filter chain, public `/api/auth/**` & `/api/public/**`, JWT resource server, `UserSyncFilter` after `BearerTokenAuthenticationFilter`, `RestTemplate` |
| `UserSyncFilter.java` | After JWT auth: if `users` row missing, insert from JWT claims (`sub`, email, given/family name); in-memory id cache |
| `KeycloakJwtAuthenticationConverter.java` | Realm roles → `ROLE_*` authorities |
| `IsAdmin.java` | `@PreAuthorize("hasRole('ADMIN')")` — put on controllers/methods |
| `IsUserOrAdmin.java` | `@PreAuthorize("hasAnyRole('USER', 'ADMIN')")` — put on controllers/methods |
| `JacksonConfig.java` | `ObjectMapper` (JavaTimeModule, no timestamps, ignore unknown) |
| `WebConfig.java` | CORS for `/api/**` (Angular `localhost:4200`) |

### Resources

| File | Role |
|------|------|
| `application.yml` | Port `8081`, datasource `5435`, JPA `ddl-auto: none`, Flyway `public`, issuer `8085`, Keycloak client, gRPC `crypto-service` |
| `V1__init_schema.sql` | Schema + tables + indexes |
| `V2__insert_default_data.sql` | 7 categories + 41 providers |
| `MyServiceApplicationTests.java` | Context load test |

## Package overview

| Path | Role |
|------|------|
| `com.myservice` | Entry point |
| `com.myservice.config` | Local/dev runners (`GrpcTestRunner`) |
| `com.myservice.domain.model` | Domain models |
| `com.myservice.domain.ports.in` | `AuthUseCase` |
| `com.myservice.domain.ports.out` | Repository & encryption ports |
| `com.myservice.application.ports.in` | Category / provider / account use cases |
| `com.myservice.application.service` | Use-case implementations |
| `com.myservice.infrastructure.adapters.in.web` | Test / misc web adapters |
| `com.myservice.infrastructure.adapters.in.web.controller` | REST controllers (`/api/auth`, `/api/v1/*`) |
| `com.myservice.infrastructure.adapters.in.web.dto.*` | HTTP DTOs |
| `com.myservice.infrastructure.adapters.in.web.mapper` | Web mappers |
| `com.myservice.infrastructure.adapters.out.grpc` | Encryption gRPC adapter |
| `com.myservice.infrastructure.adapters.out.persistence.*` | JPA adapters / entities / repos |
| `com.myservice.infrastructure.config.security` | Security, JWT converter, JIT `UserSyncFilter` |
| `com.myservice.infrastructure.config.web` | Jackson & CORS |
| `src/main/proto` | gRPC protobuf |
| `src/main/resources/db/migration` | Flyway SQL |

## Root files

| File | Description |
|------|-------------|
| `pom.xml` | Web MVC, OAuth2 resource server, JPA, PostgreSQL, `spring-boot-starter-flyway`, Validation, Lombok, gRPC client, protobuf plugin |
| `Dockerfile` | Multi-stage build (Temurin 21) |
| `mvnw` / `mvnw.cmd` | Maven Wrapper |
| `.dockerignore` | Docker build exclusions |
| `BACKEND_STRUCTURE.md` | This structure reference |
