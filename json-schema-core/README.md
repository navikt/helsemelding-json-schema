# json-schema-core

Core library for Helsemelding JSON Schemas.

## Purpose

Provides:

* Schema source models
* JSON Schema generation using `kotlinx-schema`
* Versioned schema artifacts
* Validation of JSON payloads against published schemas

## Schema Workflow

```text
Kotlin model
    ↓
kotlinx-schema
    ↓
Generated schema
    ↓
Published schema (*.schema.json)
```

Published schemas are committed to source control and represent the official contract.

## Publishing a New Schema Version

1. Update the schema model
2. Increment `@SchemaVersion`
3. Generate schema artifact with `./gradlew publishJsonSchemas`
4. Commit generated `.schema.json`

Example:

```text
schemas/outgoing-dialog-message-v2.schema.json
```

## Validation

```kotlin
val validator = JsonSchemaValidator()

validator.validate(
    schemaType = SchemaType.OUTGOING_DIALOG_MESSAGE,
    payload = json
)
```