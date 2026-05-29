# BantayBaha Frontend

Ionic Angular mobile-first app for citizens and admins.

## Current Foundation

- Angular standalone app bootstrap
- Ionic Angular shell and routing
- Leaflet map screen
- Typed project API service
- Public map route at `/map`
- Local API base URL: `http://localhost:8080/api`

## Planned Feature Areas

- `auth` - login/register flows
- `map` - Leaflet project map, search, filters
- `reports` - report submission and tracking
- `dashboard` - citizen overview and project status summaries
- `admin` - moderation, project management, analytics
- `profile` - account and user settings

## UI Direction

Modern civic-tech interface inspired by mapping tools and clean govtech dashboards, using deep blue `#0B3C5D` and cyan `#328CC1` as anchors.

## Local Setup

From the `frontend` folder:

```powershell
npm install
npm run start
```

Then open:

```plaintext
http://localhost:4200
```

The map page calls:

```plaintext
GET http://localhost:8080/api/projects
```

Run the backend and PostgreSQL first if you want seeded project markers to appear.
