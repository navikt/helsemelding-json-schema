package no.nav.helsemelding.jsonschema.core.validation

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import io.github.optimumcode.json.schema.JsonSchema
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.loader.JsonSchemaLoader
import no.nav.helsemelding.jsonschema.core.loader.SchemaLoader
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.JsonSchemaDocumentRepository
import io.github.optimumcode.json.schema.ValidationError as SchemaValidationError

interface SchemaValidator {
    fun validate(
        schemaType: SchemaType,
        json: String
    ): Either<ValidationError, String>
}

class JsonSchemaValidator internal constructor(
    private val schemaLoader: SchemaLoader
) : SchemaValidator {

    constructor() : this(
        schemaLoader = JsonSchemaLoader(
            JsonSchemaDocumentRepository()
        )
    )

    override fun validate(
        schemaType: SchemaType,
        json: String
    ): Either<ValidationError, String> = either {
        val jsonElement = parseJson(schemaType, json).bind()
        val version = extractVersion(schemaType, jsonElement).bind()
        val schema = resolveSchema(schemaType, version).bind()

        validateAgainstSchema(
            schemaType = schemaType,
            version = version,
            schema = schema,
            jsonElement = jsonElement
        ).bind()

        json
    }

    private fun resolveSchema(
        schemaType: SchemaType,
        version: Int
    ): Either<ValidationError, JsonSchema> =
        schemaLoader
            .load(schemaType, version)
            .mapLeft { schemaError ->
                schemaError.toValidationError()
            }

    private fun SchemaError.toValidationError(): ValidationError =
        when (this) {
            is SchemaError.NotFound ->
                validationError(
                    schemaType = schemaType,
                    version = version,
                    "Schema resource not found: $schemaType v$version"
                )

            is SchemaError.NoSchemasFound ->
                validationError(
                    schemaType = schemaType,
                    "No schemas found for message type: $schemaType"
                )

            is SchemaError.InvalidSchema ->
                validationError(
                    schemaType = schemaType,
                    version = version,
                    "Invalid schema: $reason"
                )
        }

    private fun parseJson(
        schemaType: SchemaType,
        payload: String
    ): Either<ValidationError, JsonElement> =
        Either.catch { Json.parseToJsonElement(payload) }
            .mapLeft { t ->
                validationError(
                    schemaType = schemaType,
                    "Invalid JSON: ${t.message}"
                )
            }

    private fun extractVersion(
        schemaType: SchemaType,
        jsonElement: JsonElement
    ): Either<ValidationError, Int> = either {
        val version = jsonElement
            .jsonObject["version"]
            ?.jsonPrimitive
            ?.intOrNull

        ensureNotNull(version) {
            validationError(
                schemaType = schemaType,
                "Missing or invalid version field"
            )
        }
    }

    private fun validateAgainstSchema(
        schemaType: SchemaType,
        version: Int,
        schema: JsonSchema,
        jsonElement: JsonElement
    ): Either<ValidationError, Unit> = either {
        val schemaErrors = mutableListOf<SchemaValidationError>()

        ensure(schema.validate(jsonElement, schemaErrors::add)) {
            ValidationError(
                schemaType = schemaType,
                version = version,
                errors = schemaErrors.map { it.message }
            )
        }
    }

    private fun validationError(
        schemaType: SchemaType,
        version: Int,
        vararg errors: String
    ) = ValidationError(
        schemaType = schemaType,
        version = version,
        errors = errors.toList()
    )

    private fun validationError(
        schemaType: SchemaType,
        vararg errors: String
    ) = ValidationError(
        schemaType = schemaType,
        version = null,
        errors = errors.toList()
    )
}
