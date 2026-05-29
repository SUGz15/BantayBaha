# BantayBaha — Master Project Context

## Project Name
BantayBaha

## Project Type
Full-Stack Civic-Tech Platform

## Project Goal
BantayBaha is a transparency and community verification platform for monitoring flood-control infrastructure projects in the Philippines.

The system allows citizens to:
- view flood-control projects on a map,
- verify whether projects are completed,
- upload geotagged reports and evidence,
- monitor infrastructure transparency,
- and help improve public accountability.

The project focuses on:
- transparency,
- disaster resilience,
- civic engagement,
- and infrastructure accountability.

---

# Core Problem

Flood-control projects are sometimes:
- unfinished,
- delayed,
- damaged,
- or allegedly marked as completed despite incomplete implementation.

Citizens currently lack:
- centralized transparency tools,
- public verification systems,
- and accessible monitoring platforms.

BantayBaha solves this problem using:
- crowdsourced reporting,
- GIS mapping,
- geotagged evidence,
- and transparency dashboards.

---

# MVP Scope

## Features Included in MVP

### 1. Authentication
- Register
- Login
- JWT authentication
- Role-based access

### 2. Interactive Map
- Display flood-control projects
- Project markers
- Project search
- Filter projects

### 3. Project Details
- Budget
- Contractor
- Timeline
- Completion status
- Community reports
- Uploaded images

### 4. Community Verification
- Submit reports
- Upload geotagged photos
- Add descriptions
- Track report status

### 5. Admin Dashboard
- Moderate reports
- Approve/reject reports
- Manage projects
- View basic analytics

---

# Tech Stack

## Frontend
- Ionic Angular
- TypeScript
- Angular Standalone Components
- Leaflet.js
- RxJS

## Backend
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- REST APIs

## Database
- PostgreSQL
- PostGIS

## Cloud Storage
- Cloudinary

---

# Architecture Approach

## Backend Architecture
Use:
- modular clean architecture,
- feature-based organization,
- layered architecture.

Architecture pattern:

Controller
→ Service
→ Repository
→ Database

Rules:
- Controllers should remain thin.
- Business logic belongs in services.
- Database logic belongs in repositories.
- Use DTOs for request/response.
- Use mapper classes.
- Use validation annotations.
- Use global exception handling.
- Follow SOLID principles when practical.
- Avoid over-engineering.

DO NOT implement:
- microservices,
- CQRS,
- event sourcing,
- Kafka,
- unnecessary abstractions.

Keep architecture practical and maintainable.

---

# Backend Folder Structure

```plaintext
backend/
│
├── config/
├── security/
├── exception/
├── util/
│
├── auth/
│   ├── controller/
│   ├── dto/
│   ├── service/
│   └── security/
│
├── project/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── mapper/
│
├── report/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── mapper/
│
└── user/
    ├── controller/
    ├── dto/
    ├── entity/
    ├── repository/
    ├── service/
    └── mapper/

    Frontend Architecture
Frontend Structure
src/app/
│
├── core/
├── shared/
├── features/
│   ├── auth/
│   ├── map/
│   ├── reports/
│   ├── dashboard/
│   ├── admin/
│   └── profile/

Rules:

Use standalone Angular components.
Use reusable shared components.
Use reactive forms.
Keep UI mobile-first.
Use feature-based organization.
Keep components modular and reusable.
Use services for API communication.
UI/UX Direction

The platform should feel:

modern,
trustworthy,
clean,
informative,
civic-tech focused,
mobile-first.

Avoid:

outdated government-style UI,
cluttered dashboards,
overcomplicated interfaces.

Design inspiration:

Google Maps
Waze
Modern govtech dashboards

Primary Colors:

Deep Blue (#0B3C5D)
Cyan (#328CC1)
User Types
Citizen

Can:

view projects,
submit reports,
upload photos,
verify infrastructure status.
Admin

Can:

moderate reports,
manage projects,
manage users,
update statuses.
Database Tables
USERS
id
full_name
email
password_hash
role
reputation_score
PROJECTS
id
title
description
contractor
budget
status
latitude
longitude
geom
REPORTS
id
project_id
user_id
status
description
image_url
is_verified
Development Priorities
Phase 1 — Foundation
Setup frontend/backend
PostgreSQL setup
JWT authentication
User roles
Phase 2 — Interactive Map
Leaflet integration
Project markers
Project APIs
Project details page
Phase 3 — Community Reports
Report submission
Image uploads
Geotagging
Report tracking
Phase 4 — Admin Dashboard
Report moderation
Project management
Basic analytics
Coding Standards
Backend
Use DTOs.
Use ResponseEntity properly.
Use constructor injection.
Add validation annotations.
Add proper exception handling.
Keep controllers thin.
Avoid duplicated logic.
Frontend
Use strict TypeScript.
Use reactive forms.
Use lazy loading when applicable.
Keep UI responsive.
Optimize mobile performance.
Git Conventions
Branches
main
develop
feature/authentication
feature/map-system
feature/report-module
feature/admin-dashboard
Commit Format

Examples:

feat: add JWT authentication
feat: implement interactive map
fix: resolve image upload validation
refactor: optimize report service
Current Development Focus

Current priority:

Setup backend/frontend
Configure PostgreSQL
JWT authentication
Interactive map
Project API
Project details page

DO NOT start advanced features yet.

Future Features (NOT MVP)

These are future enhancements:

AI verification
Flood prediction
Satellite imagery comparison
QR code verification
Community reputation system
Advanced analytics
Push notifications

Do not implement these during MVP development.

Important Project Philosophy

The goal is:

clean architecture,
scalable foundation,
professional UI/UX,
and a completed working MVP.

Prioritize:

maintainability,
readability,
modularity,
and user experience.

Avoid unnecessary complexity.