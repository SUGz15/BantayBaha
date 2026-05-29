# BantayBaha Backend

Spring Boot REST API for authentication, flood-control projects, community reports, and admin moderation.

## Planned Modules

- `auth` - registration, login, JWT issuance, authentication helpers
- `user` - user profile, roles, account data
- `project` - flood-control project records and map data
- `report` - citizen reports, evidence, moderation status
- `config`, `security`, `exception`, `util` - shared infrastructure

## Architecture

Controllers stay thin, services contain business logic, repositories handle persistence, and DTOs/mappers keep API contracts separate from entities.
