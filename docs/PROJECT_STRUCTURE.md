# Project Structure

```plaintext
BantayBaha/
|-- backend/
|   |-- src/main/java/com/bantaybaha/
|   |   |-- config/
|   |   |-- security/
|   |   |-- exception/
|   |   |-- util/
|   |   |-- auth/
|   |   |-- user/
|   |   |-- project/
|   |   `-- report/
|   `-- src/main/resources/
|-- frontend/
|   `-- src/app/
|       |-- core/
|       |-- shared/
|       `-- features/
|           |-- auth/
|           |-- map/
|           |-- reports/
|           |-- dashboard/
|           |-- admin/
|           `-- profile/
|-- docs/
|-- BantayBaha.md
|-- docker-compose.yml
`-- .env.example
```

This structure follows the MVP priorities in `BantayBaha.md`: foundation first, then map and project APIs, then reports, then admin tools.
