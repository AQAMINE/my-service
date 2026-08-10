# my-service — Backend Structure

Spring Boot backend (`com.my.service` / `com.myservice`), Java 17, Maven.
Auth via Keycloak (OAuth2 resource server). Schema managed with Flyway (PostgreSQL).
Hexagonal architecture (ports & adapters).

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
    │   │           │   │   └── AuthTokens.java
    │   │           │   └── ports/                      # Contrats / Interfaces du domaine
    │   │           │       ├── in/                     # Use Cases (Cas d'utilisation)
    │   │           │       │   └── AuthUseCase.java
    │   │           │       └── out/                    # Ports sortants (repositories, gateways)
    │   │           │
    │   │           ├── application/                    # Implémentations / Services Métier
    │   │           │   └── service/
    │   │           │       └── KeycloakAuthServiceImpl.java
    │   │           │
    │   │           └── infrastructure/                 # Adaptateurs & Framework (Spring/Keycloak)
    │   │               ├── adapters/
    │   │               │   └── in/
    │   │               │       └── web/                # Contrôleurs REST & DTOs
    │   │               │           ├── AuthController.java
    │   │               │           ├── TestController.java
    │   │               │           └── dto/
    │   │               │               ├── request/
    │   │               │               │   ├── LoginRequest.java
    │   │               │               │   └── RefreshTokenRequest.java
    │   │               │               └── response/
    │   │               │                   └── AuthResponse.java
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

## Package overview

| Path | Role |
|------|------|
| `com.myservice` | Application entry point |
| `com.myservice.domain.model` | Domain entities / models (`AuthTokens`) |
| `com.myservice.domain.exception` | Domain / business exceptions |
| `com.myservice.domain.ports.in` | Inbound ports / use cases (`AuthUseCase` — primitives in, domain model out) |
| `com.myservice.domain.ports.out` | Outbound ports (repositories, external gateways) |
| `com.myservice.application.service` | Use-case implementations (`KeycloakAuthServiceImpl`) |
| `com.myservice.infrastructure.adapters.in.web` | REST controllers |
| `com.myservice.infrastructure.adapters.in.web.dto.request` | Web request DTOs |
| `com.myservice.infrastructure.adapters.in.web.dto.response` | Web response DTOs |
| `com.myservice.infrastructure.config` | Spring Security & JWT config |
| `src/main/resources/db/migration` | Flyway SQL migrations (PostgreSQL) |
| `src/test/java` | Unit / integration tests |

## Architecture layers

| Layer | Package | Purpose |
|-------|---------|---------|
| Domain | `domain.model`, `domain.exception`, `domain.ports.in`, `domain.ports.out` | Models, exceptions, inbound & outbound ports (no infra DTOs) |
| Application | `application.service` | Use-case implementations |
| Infrastructure | `infrastructure.adapters`, `infrastructure.config` | Web adapters map DTOs ↔ domain; Spring/Keycloak wiring |

## File migration map

| Old path | New path |
|----------|----------|
| `auth/service/AuthService.java` | `domain/ports/in/AuthUseCase.java` |
| `auth/service/impl/KeycloakAuthServiceImpl.java` | `application/service/KeycloakAuthServiceImpl.java` |
| `auth/controller/AuthController.java` | `infrastructure/adapters/in/web/AuthController.java` |
| `controller/TestController.java` | `infrastructure/adapters/in/web/TestController.java` |
| `auth/dto/request/LoginRequest.java` | `infrastructure/adapters/in/web/dto/request/LoginRequest.java` |
| `auth/dto/request/RefreshTokenRequest.java` | `infrastructure/adapters/in/web/dto/request/RefreshTokenRequest.java` |
| `auth/dto/response/AuthResponse.java` | `infrastructure/adapters/in/web/dto/response/AuthResponse.java` |
| `config/SecurityConfig.java` | `infrastructure/config/SecurityConfig.java` |
| `config/KeycloakJwtAuthenticationConverter.java` | `infrastructure/config/KeycloakJwtAuthenticationConverter.java` |

## Migrations

| File | Purpose |
|------|---------|
| `V1__init_schema.sql` | Initial database schema |
| `V2__insert_default_data.sql` | Seed / default data |

## Root files

| File | Description |
|------|-------------|
| `pom.xml` | Maven dependencies and build config (incl. Flyway + Keycloak/OAuth2) |
| `Dockerfile` | Container image build |
| `mvnw` / `mvnw.cmd` | Maven Wrapper scripts |
| `.dockerignore` | Files excluded from Docker build context |
| `BACKEND_STRUCTURE.md` | This structure reference |
