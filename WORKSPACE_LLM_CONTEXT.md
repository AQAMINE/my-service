# MyService Workspace — LLM Context Document

> Hand-off document for another LLM. Describes the full multi-repo microservice platform: structure, ports, APIs, auth, databases, and how services connect.
>
> **Sibling layout expected:**
> ```
> Documents/GitHub/
> ├── my-service/           # Main Spring Boot API (passwords / accounts)
> ├── my-service-ui/        # Angular SPA
> ├── my-service-infra/     # Docker Compose + Keycloak realm
> ├── my-service-crypto/    # Python gRPC AES-GCM encryption
> └── my-service-cards/     # Spring Boot API (banks / cards)
> ```

---

## 1. Product overview

Personal **password manager** + **bank card vault**:

- Store external account credentials (encrypted passwords).
- Store bank cards (encrypted PAN / CVV / PIN).
- Admin user management via Keycloak.
- Settings for categories, providers, banks, and card networks.
- UI language: mostly French.

---

## 2. Architecture diagram

```
┌─────────────────────┐
│  my-service-ui      │  Angular 22
│  Dev:  :4200        │
│  Docker: :80        │
└─────────┬───────────┘
          │ REST + Bearer JWT
          │ (HttpOnly refresh cookie via /api/auth)
     ┌────┴────────────────────────────┐
     │                                 │
     ▼                                 ▼
┌──────────────────┐         ┌────────────────────┐
│ my-service       │         │ my-service-cards   │
│ Spring Boot 4.1  │         │ Spring Boot 4.1.1  │
│ Host: 8081       │         │ Host: 8083         │
│ Docker: 8082→8081│         │ DB: my_cards_db    │
│ DB: myservicedb  │         │                    │
└────────┬─────────┘         └─────────┬──────────┘
         │ gRPC Encrypt/Decrypt        │ gRPC Encrypt/Decrypt
         └──────────────┬──────────────┘
                        ▼
              ┌─────────────────────┐
              │ my-service-crypto   │
              │ Python gRPC AES-GCM │
              │ :50051              │
              └─────────────────────┘

Shared infra (docker-compose in my-service-infra):
  PostgreSQL 16  host:5435 → container:5432
  Keycloak 26.1  host:8085 → container:8080
  Realm: my-service-realm
```

---

## 3. Ports cheat sheet (host)

| Service | Host port | Container / app port | Protocol | Notes |
|---------|-----------|----------------------|----------|-------|
| Angular UI (`ng serve`) | **4200** | — | HTTP | Dev |
| Angular UI (Docker/Nginx) | **80** | 80 | HTTP | Production compose |
| my-service (local) | **8081** | 8081 | HTTP REST | `application.yml` |
| my-service (Docker) | **8082** | 8081 | HTTP REST | Compose maps 8082→8081 |
| my-service-cards (local) | **8083** | 8083 | HTTP REST | `application.yaml` |
| my-service-cards (compose intent) | **8083** | maps to **8081** | HTTP REST | Port mismatch vs yaml |
| Keycloak | **8085** | 8080 | HTTP | Admin: admin/admin |
| PostgreSQL | **5435** | 5432 | TCP | User: postgres |
| my-service-crypto | **50051** | 50051 | gRPC plaintext | AES-GCM |

---

## 4. How services connect

| From → To | Mechanism | Target |
|-----------|-----------|--------|
| UI → my-service | REST | `http://localhost:8081/api` |
| UI → my-service-cards | REST | `http://localhost:8083/api` |
| UI → Keycloak | Indirect | Login via my-service password grant |
| my-service → Keycloak | OIDC JWT + Admin API | Issuer + client `my-service-backend` |
| my-service-cards → Keycloak | JWT resource server | Same realm JWTs |
| my-service → crypto | gRPC client `crypto-service` | Encrypt/decrypt passwords |
| my-service-cards → crypto | gRPC client `crypto-service` | Encrypt/decrypt PAN/CVV/PIN |
| Docker Nginx → backend | `proxy_pass` | Only `/api` → `my-service-backend:8081` |
| Both backends → Postgres | JDBC | Separate DBs |

### Auth flow

1. UI `POST /api/auth/login` → my-service → Keycloak token endpoint (Resource Owner Password + client secret).
2. Access token returned in JSON body (kept in Angular memory signal).
3. Refresh token set as **HttpOnly** cookie `refreshToken` (path `/api/auth`).
4. JWT interceptor adds `Authorization: Bearer` + `X-User-Id` (JWT `sub`).
5. `UserSyncFilter` (main backend only) JIT-inserts user into `users` on first authenticated request.
6. Realm roles: `USER`, `ADMIN` → Spring `ROLE_USER` / `ROLE_ADMIN`.

---

# Repo 1: `my-service` (main backend)

**Path:** `my-service/`  
**Role:** Auth proxy to Keycloak, user admin, categories, providers, encrypted external accounts (password manager API).

## Tech stack

| Item | Version / choice |
|------|------------------|
| Language | Java **21** |
| Framework | Spring Boot **4.1.0** |
| Build | Maven (`pom.xml`, `mvnw`) |
| Security | OAuth2 Resource Server + Keycloak Admin Client **24.0.0** |
| Persistence | Spring Data JPA + PostgreSQL + Flyway |
| gRPC | `grpc-client-spring-boot-starter` **3.1.0**, protoc **3.25.5**, grpc-java **1.68.2** |
| Other | Lombok, Jackson, Validation |

## Hexagonal architecture

```
com.myservice
├── domain/
│   ├── model/           # AccountCategory, AuthTokens, ExternalAccount, Provider, User
│   ├── ports/in/        # AuthUseCase
│   ├── ports/out/       # *RepositoryPort, EncryptionServicePort
│   └── exception/
├── application/
│   ├── ports/in/        # AccountCategoryUseCase, ExternalAccountUseCase, ProviderUseCase
│   └── service/         # *Service, KeycloakAuthServiceImpl, UserManagementService
└── infrastructure/
    ├── adapters/in/web/           # Controllers, DTOs, mappers
    ├── adapters/out/grpc/         # EncryptionGrpcAdapter
    ├── adapters/out/persistence/  # entities, Spring Data repos, adapters, mappers
    └── config/                    # security, web, database, cache, grpc placeholders
```

## Directory structure

```
my-service/
├── Dockerfile
├── pom.xml
├── BACKEND_STRUCTURE.md
├── WORKSPACE_LLM_CONTEXT.md   # this file
└── src/main/
    ├── java/com/myservice/
    ├── proto/crypto.proto
    └── resources/
        ├── application.yml
        └── db/migration/
            ├── V1__init_schema.sql
            └── V2__insert_default_data.sql
```

## Config (`application.yml`)

| Key | Value |
|-----|-------|
| `server.port` | `8081` |
| `spring.application.name` | `my-service` |
| Datasource | `jdbc:postgresql://localhost:5435/myservicedb` |
| DB user/password | `postgres` / `postgrespassword` |
| `jpa.hibernate.ddl-auto` | `none` (Flyway owns schema) |
| JWT issuer | `http://localhost:8085/realms/my-service-realm` |
| Keycloak server | `http://localhost:8085` |
| Keycloak realm | `my-service-realm` |
| Keycloak client | `my-service-backend` |
| Keycloak client-secret | `nBBDagDMZTnaCMXm1bK4WRnrYZYQf5jd` |
| Keycloak admin | `admin` / `admin` |
| gRPC crypto | `static://localhost:50051` (plaintext) |

Docker env overrides: `SPRING_DATASOURCE_*`, JWT issuer URI, `GRPC_CLIENT_CRYPTO_HOST/PORT`.

## REST API

| Method | Path | Auth | Notes |
|--------|------|------|-------|
| `POST` | `/api/auth/login` | Public | `{username,password}`; sets refresh cookie |
| `POST` | `/api/auth/refresh` | Cookie | Reads `refreshToken` cookie |
| `POST` | `/api/auth/logout` | Public | Clears cookie |
| `GET` | `/api/public/hello` | Public | Smoke test |
| `GET` | `/api/private/hello` | JWT | Smoke test |
| `GET/POST` | `/api/v1/categories` | JWT + `X-User-Id` | |
| `DELETE` | `/api/v1/categories/{id}` | JWT | |
| `GET/POST` | `/api/v1/providers` | JWT + `X-User-Id` | |
| `DELETE` | `/api/v1/providers/{id}` | JWT | |
| `GET` | `/api/v1/accounts` | JWT + `X-User-Id` | Optional `?categoryId` / `?providerId` |
| `GET` | `/api/v1/accounts/{id}` | JWT + `X-User-Id` | |
| `GET` | `/api/v1/accounts/{id}/password` | JWT + `X-User-Id` | Decrypt via gRPC → `{password}` |
| `POST` | `/api/v1/accounts` | JWT + `X-User-Id` | Encrypts `rawPassword` |
| `DELETE` | `/api/v1/accounts/{id}` | JWT | |
| `POST` | `/api/v1/users` | JWT + `@IsAdmin` | Create in Keycloak |
| `GET` | `/api/v1/users` | JWT + `@IsAdmin` | List Keycloak users |
| `PATCH` | `/api/v1/users/{id}/status?enabled=` | JWT + `@IsAdmin` | Enable/disable |

## Database (`myservicedb`)

| Table | Purpose |
|-------|---------|
| `users` | `id` UUID = Keycloak `sub`; email, names, preferences JSONB, `is_active` |
| `account_categories` | Global (`user_id NULL`) or user-owned; unique `(user_id, slug)` |
| `providers` | Same pattern; `website_url`, `color`, `logo_url` |
| `external_accounts` | Links user/category/provider; `encrypted_password` + `encryption_iv` |

Seed (V2): ~7 system categories, ~41 system providers (Google, GitHub, Netflix, PayPal, …).

## Security notes

- Stateless JWT resource server; realm roles → `ROLE_*` via `KeycloakJwtAuthenticationConverter`.
- Public: `/api/public/**`, `/api/auth/**`, OPTIONS.
- `@IsAdmin` → `hasRole('ADMIN')`; `@IsUserOrAdmin` → `USER` or `ADMIN`.
- CORS: `http://localhost:4200`, credentials allowed.
- `UserSyncFilter` after Bearer auth — JIT user sync.
- Docker: multi-stage Maven → Temurin 21 JRE Alpine, EXPOSE **8081**, non-root user `spring`.

---

# Repo 2: `my-service-ui` (frontend)

**Path:** `my-service-ui/`  
**Role:** Angular SPA — login, dashboard, password manager, cards manager, admin, settings.

## Tech stack

| Item | Version |
|------|---------|
| Angular | **^22.1.0** |
| TypeScript | **~6.0.2** |
| RxJS | **~7.8.0** |
| SSR | `@angular/ssr` + Express **^5.1.0** |
| Icons | `simple-icons` |
| Tests | Vitest |

## Ports / URLs

| Context | Value |
|---------|-------|
| Dev server | **4200** |
| Docker Nginx | **80** |
| `environment.apiUrl` | `http://localhost:8081/api` |
| `environment.cardsApiUrl` | `http://localhost:8083/api` |
| Dev proxy `/api` | → `http://localhost:8081` |

## Feature folders

```
src/app/features/
├── admin/              # User management (adminGuard)
├── auth/               # Login
├── cards-manager/      # Bank cards vault
├── dashboard/          # Tiles / navigation hub
├── password-manager/   # External accounts vault
└── settings/
    └── page/
        ├── banks/
        ├── card-providers/
        ├── categories/
        └── providers/
```

## Routing

| Path | Feature | Guard |
|------|---------|-------|
| `/login` | Login | — |
| `/dashboard` | Dashboard | `authGuard` |
| `/password-manager` | Accounts vault | `authGuard` |
| `/cards-manager` | Bank cards | `authGuard` |
| `/admin/**` | Admin shell | `authGuard` + `adminGuard` |
| `/admin/user-management` | User CRUD | admin |
| `/settings/categories` | Categories | `authGuard` |
| `/settings/providers` | Providers | `authGuard` |
| `/settings/card-providers` | Card networks | `authGuard` |
| `/settings/banks` | Banks | `authGuard` |
| `/` | → `/login` | — |

## Auth (UI)

1. `APP_INITIALIZER` calls `refreshToken()` on boot (HttpOnly cookie).
2. Login → `${apiUrl}/auth/login`; stores access token in signal.
3. `jwtInterceptor`: Bearer + `X-User-Id` + `withCredentials: true`.
4. Roles from JWT `realm_access.roles`; admin if `ADMIN` / `ROLE_ADMIN`.
5. Logout → `/api/auth/logout` then navigate to login.

## Docker

Node 24 build → `nginx:alpine`. Nginx proxies `/api` **only** to `my-service-backend:8081` (cards API is **not** proxied in Docker).

---

# Repo 3: `my-service-infra` (orchestration)

**Path:** `my-service-infra/`  
**Role:** Docker Compose + Keycloak realm import artifacts.

## docker-compose services

| Service | Image / build | Host ports | Depends on |
|---------|---------------|------------|------------|
| `postgres` | `postgres:16-alpine` | **5435:5432** | — (healthcheck) |
| `keycloak` | `quay.io/keycloak/keycloak:26.1.0` | **8085:8080** | — |
| `my-service-crypto` | build `../my-service-crypto` | **50051:50051** | — |
| `my-service-backend` | build `../my-service` | **8082:8081** | postgres, keycloak, crypto |
| `my-service-frontend` | build `../my-service-ui` | **80:80** | backend |

**Network:** `app-network` (bridge)  
**Volumes:** `postgres_data`, `keycloak_data`, `keycloak_imports`

### Env highlights

- Postgres: DB `myservicedb`, user/password `postgres` / `postgrespassword`
- Keycloak: admin `admin`/`admin`, `start-dev --import-realm --insecure`
- Crypto: `GRPC_PORT=50051`, `CRYPTO_SECRET_KEY` (base64 32-byte key)
- Backend JWT issuer in compose: `http://keycloak:8080/realms/my-realm` ⚠️ (see known issues)

### Keycloak realm (`imports/realm-export.json`)

| Setting | Value |
|---------|-------|
| Realm | **`my-service-realm`** |
| SSL | `none` |
| Registration | disabled |
| Access token lifespan | **7200s** |
| Realm roles | `USER`, `ADMIN` |
| Client | `my-service-backend` — confidential, direct access grants, service accounts |

## Known infra issues

1. **`my-service-cards` block is incorrectly indented under `networks`** → not a valid Compose service.
2. **No Dockerfile** in `my-service-cards` (compose expects one).
3. Postgres only creates **`myservicedb`**; **`my_cards_db` must be created manually**.
4. Realm name mismatch: Docker backend issuer uses **`my-realm`**, everywhere else **`my-service-realm`**.
5. Cards Keycloak client (`my-service-cards`) configured in yaml but **missing from realm export**.
6. Keycloak import uses a **named volume**, not a bind mount to `./imports` — realm may not auto-import.
7. Nginx proxies only main backend; Docker UI cannot reach cards API without an extra proxy.
8. Cards compose maps `8083:8081` but app listens on **8083**.

---

# Repo 4: `my-service-crypto` (encryption)

**Path:** `my-service-crypto/`  
**Role:** Shared AES-256-GCM encrypt/decrypt over gRPC. Used by both Spring backends.

## Tech stack

- Python **3.11**
- `grpcio==1.62.0`, `grpcio-tools==1.62.0`, `cryptography==42.0.5`, `protobuf==4.25.3`

## Structure

```
my-service-crypto/
├── Dockerfile
├── requirements.txt
├── proto/crypto.proto
└── src/
    ├── server.py           # gRPC server
    └── crypto_service.py   # AESGCMCrypto
```

## Port / env

| Variable | Default |
|----------|---------|
| `GRPC_PORT` | **50051** |
| `CRYPTO_SECRET_KEY` | Base64-encoded 32-byte key (else hard-coded dev key) |

## gRPC API (`CryptoService`)

- `Encrypt(EncryptRequest{plain_text})` → `{cipher_text_base64, iv_base64}`
- `Decrypt(DecryptRequest{cipher_text_base64, iv_base64})` → `{plain_text}`
- Insecure (no TLS): `[::]:50051`

> **Proto note:** Main service uses `iv_base64`. Cards’ proto may use `iv_b64` — keep stubs consistent with the running server.

---

# Repo 5: `my-service-cards` (cards backend)

**Path:** `my-service-cards/`  
**Role:** Bank card vault — banks, card networks, encrypted bank cards.

## Tech stack

| Item | Version |
|------|---------|
| Java | **21** |
| Spring Boot | **4.1.1** |
| Same pattern | JPA, Flyway, OAuth2 RS, Keycloak admin client, gRPC client |

## Architecture packages

```
com.myservicecards.my_service_cards
├── domain/model + domain/service + domain/exception
├── ports/in + ports/out
└── infrastructure/adapter/
    ├── in/web/          # Controllers, DTOs
    ├── out/grpc/        # Encryption
    ├── out/persistence/ # entities, repos, adapters, mappers
    └── config/security/
```

## Config (`application.yaml`)

| Key | Value |
|-----|-------|
| `server.port` | **8083** |
| Datasource | `jdbc:postgresql://localhost:5435/my_cards_db` |
| JWT issuer | `http://localhost:8085/realms/my-service-realm` |
| Keycloak client | `my-service-cards` |
| Keycloak client-secret | `b5fy4R36oAG6bXUpQK0P461P2gZeBItC` |
| gRPC | `localhost:50051` plaintext |

## REST API

| Method | Path | Notes |
|--------|------|-------|
| `GET/POST` | `/api/v1/banks` | JWT `sub` as user id |
| `DELETE` | `/api/v1/banks/{id}` | |
| `GET/POST` | `/api/v1/card-providers` | |
| `DELETE` | `/api/v1/card-providers/{id}` | |
| `GET/POST` | `/api/v1/cards` | |
| `GET` | `/api/v1/cards/{cardId}/reveal-pan` | → `{pan}` |
| `GET` | `/api/v1/cards/{cardId}/reveal-cvv` | → `{cvv}` |
| `GET` | `/api/v1/cards/{cardId}/reveal-pin` | → `{pin}` |
| `DELETE` | `/api/v1/cards/{cardId}` | |

All authenticated (only `/actuator/**` public). CORS: `http://localhost:4200`.  
User identity from JWT `sub` (not `X-User-Id`).

## Database (`my_cards_db`)

| Table | Purpose |
|-------|---------|
| `banks` | System + custom; Moroccan banks + Revolut/Wise/N26/… seeded |
| `card_providers` | Visa, Mastercard, Amex, Discover, JCB |
| `bank_cards` | Holder, last4, expiry, colors; encrypted PAN/CVV/PIN + IVs |

**No Dockerfile** in repo yet.

---

## 5. Cross-cutting domain concepts

- **System vs user-owned** resources: `user_id IS NULL` = shared defaults (categories, providers, banks, card providers).
- Secrets never stored plaintext — always AES-GCM via `my-service-crypto`.
- Admin user lifecycle lives in Keycloak; main DB mirrors via JIT sync.
- Main API identity header: `X-User-Id`. Cards API: JWT `sub` only.

---

## 6. Local quick start

1. Start Postgres + Keycloak (+ crypto) via `my-service-infra` / locally.
2. Ensure realm `my-service-realm` + client `my-service-backend` exist.
3. Create DB `my_cards_db` if using cards (and Keycloak client `my-service-cards` if needed).
4. Run crypto on **50051**.
5. Run `my-service` on **8081**, `my-service-cards` on **8083**.
6. `npm start` UI on **4200**.

Or from infra (partial, cards currently broken in compose):

```bash
cd my-service-infra
docker compose up -d
```

---

## 7. File to point another LLM at

Use this file as system/context:

```
my-service/WORKSPACE_LLM_CONTEXT.md
```

Related in-repo docs:

- `my-service/BACKEND_STRUCTURE.md`
- `my-service-cards/HEXAGONAL.md` (if present)
- `my-service-infra/README.md`
- `my-service-ui/README.md`
