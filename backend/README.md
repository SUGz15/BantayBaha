# BantayBaha Backend

Spring Boot REST API for authentication, flood-control projects, community reports, and admin moderation.

## Current Foundation

- Java 21
- Spring Boot 3.3.5
- Spring Web, Security, Validation, Data JPA
- PostgreSQL driver with Hibernate Spatial
- Flyway migration for PostGIS
- JWT libraries ready for authentication work
- Public health endpoint at `GET /api/health`

## Planned Modules

- `auth` - registration, login, JWT issuance, authentication helpers
- `user` - user profile, roles, account data
- `project` - flood-control project records and map data
- `report` - citizen reports, evidence, moderation status
- `config`, `security`, `exception`, `util` - shared infrastructure

## Architecture

Controllers stay thin, services contain business logic, repositories handle persistence, and DTOs/mappers keep API contracts separate from entities.

## Local Setup

From the repository root:

```powershell
docker compose up -d postgres
```

From the `backend` folder:

```powershell
mvn spring-boot:run
```

Then check:

```plaintext
GET http://localhost:8080/api/health
```

## Notes

Maven must be installed or supplied through an IDE before the backend can be run from the command line.

Authentication is now available through:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

Project map reads are now available through:

- `GET /api/projects`
- `GET /api/projects?status=COMPLETED`
- `GET /api/projects?search=river`
- `GET /api/projects/{id}`

Project read endpoints are public to support transparency and map browsing.

Community reports are now available through:

- `POST /api/projects/{projectId}/reports`
- `GET /api/projects/{projectId}/reports`
- `GET /api/reports/me`

Report submission requires authentication. Project report lists are public so project detail pages can show community evidence. The next backend milestone is admin moderation: approving/rejecting reports and preparing project management endpoints.

Admin report moderation is now available through:

- `GET /api/admin/reports/pending`
- `POST /api/admin/reports/{reportId}/approve`
- `POST /api/admin/reports/{reportId}/reject`

Admin endpoints require the `ADMIN` role. Moderation stores the moderator, reason, timestamp, report status, and verification result.

Admin project management is now available through:

- `POST /api/admin/projects`
- `PUT /api/admin/projects/{id}`

Project management endpoints require the `ADMIN` role. Local development also gets seeded map data through Flyway so the frontend can immediately render sample flood-control projects.

The next milestone is frontend initialization: Ionic Angular routing, auth screens, map screen shell, and API services wired to these backend endpoints.
