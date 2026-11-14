# Aegis Vault

[![CI](https://github.com/YOUR_GITHUB_USER/my-service/actions/workflows/ci.yml/badge.svg)](https://github.com/YOUR_GITHUB_USER/my-service/actions/workflows/ci.yml)

**Aegis** is a personal security suite (lab / portfolio project): encrypted password vault, bank-card vault, and Kanban tasks — behind Keycloak SSO.

> Not a production fintech product. Built to demonstrate microservices architecture, hexagonal design, and sensitive-data handling patterns.

## Architecture

| Repo | Role | Port |
|------|------|------|
| `my-service` (this repo) | **Aegis Vault** — auth proxy, users, categories, encrypted credentials | `8081` |
| `my-service-cards` | **Aegis Cards** — bank card vault (PAN/CVV/PIN encrypted) | `8083` |
| `my-service-tasks` | **Aegis Tasks** — Kanban task manager | `8084` |
| `my-service-crypto` | **Aegis Crypto** — Python gRPC AES-256-GCM | `50051` |
| `my-service-ui` | Angular SPA | `4200` / Docker `:80` |
| `my-service-infra` | Docker Compose (Postgres, Keycloak, Redis, apps) | — |

```
UI (Angular) ──JWT──► Vault / Cards / Tasks (Spring Boot 4, Java 21)
                           │
                           ├── PostgreSQL + Flyway
                           ├── Redis (cache)
                           ├── Keycloak (OAuth2 resource server)
                           └── gRPC ► Crypto (AES-256-GCM)
```

## Stack highlights

- Java 21, Spring Boot 4, hexagonal architecture (ports & adapters)
- Keycloak OAuth2/JWT, role-based access (`USER` / `ADMIN`)
- Secrets never stored in plaintext — encrypt/decrypt via dedicated crypto service
- Flyway-owned schema, Redis cache, OpenAPI
- Docker Compose multi-service local stack

## Security notes (local)

- **No secrets in git.** Copy `.env.example` → `.env` (gitignored) and set values.
- Crypto refuses to start without `CRYPTO_SECRET_KEY` (base64 32-byte key).
- Align Keycloak client secrets with your realm configuration.

## Quick start (API only)

```bash
cp .env.example .env
# fill SPRING_DATASOURCE_PASSWORD, SPRING_REDIS_PASSWORD, KEYCLOAK_CLIENT_SECRET, …

export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run
```

Full stack: see `my-service-infra` (`cp .env.example .env && docker compose up --build`).

## Tests & CI

```bash
./mvnw -B test
```

GitHub Actions runs unit tests on push/PR (see `.github/workflows/ci.yml`).

## Portfolio framing

Present Aegis as a **personal security suite / architecture lab**: microservices boundaries, hexagonal backends, Keycloak, and a dedicated crypto service — not as a hardened commercial vault.
