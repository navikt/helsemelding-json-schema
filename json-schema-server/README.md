# json-schema-server

HTTP service exposing published Helsemelding JSON Schemas.

## Purpose

Provides:

- Schema discovery
- Schema version lookup
- Access to published JSON Schemas

## API Documentation

Interactive API documentation is available through Swagger UI:

```text
/swagger
```

## Schema Source

Schemas are loaded from the `json-schema-core` module during application startup and served from memory.