# my-service — Backend Structure

Spring Boot backend (`com.my.service` / `com.myservice`), Java 17, Maven.

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
    │   │           ├── auth/
    │   │           │   ├── controller/
    │   │           │   │   └── AuthController.java
    │   │           │   ├── dto/
    │   │           │   │   ├── request/
    │   │           │   │   │   ├── LoginRequest.java
    │   │           │   │   │   └── RefreshTokenRequest.java
    │   │           │   │   └── response/
    │   │           │   │       └── AuthResponse.java
    │   │           │   └── service/
    │   │           │       ├── AuthService.java
    │   │           │       └── impl/
    │   │           │           └── KeycloakAuthServiceImpl.java
    │   │           ├── config/
    │   │           │   ├── KeycloakJwtAuthenticationConverter.java
    │   │           │   └── SecurityConfig.java
    │   │           └── controller/
    │   │               └── TestController.java
    │   └── resources/
    │       ├── application.yml
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
| `com.myservice.auth.controller` | Auth HTTP endpoints |
| `com.myservice.auth.dto.request` | Auth request DTOs |
| `com.myservice.auth.dto.response` | Auth response DTOs |
| `com.myservice.auth.service` | Auth service interface |
| `com.myservice.auth.service.impl` | Keycloak-backed auth implementation |
| `com.myservice.config` | Security & JWT configuration |
| `com.myservice.controller` | General / test controllers |
| `src/main/resources` | Config (`application.yml`), static assets, templates |
| `src/test/java` | Unit / integration tests |

## Root files

| File | Description |
|------|-------------|
| `pom.xml` | Maven dependencies and build config |
| `Dockerfile` | Container image build |
| `mvnw` / `mvnw.cmd` | Maven Wrapper scripts |
| `.dockerignore` | Files excluded from Docker build context |
