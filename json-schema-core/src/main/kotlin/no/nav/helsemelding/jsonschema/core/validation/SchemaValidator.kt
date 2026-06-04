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
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.JsonSchemaDocumentRepository
import no.nav.helsemelding.jsonschema.core.repository.JsonSchemaRepository
import no.nav.helsemelding.jsonschema.core.repository.SchemaRepository
import io.github.optimumcode.json.schema.ValidationError as SchemaValidationError

interface SchemaValidator {
    fun validate(
        schemaType: SchemaType,
        json: String
    ): Either<ValidationError, String>
}

class JsonSchemaValidator internal constructor(
    private val schemaRepository: SchemaRepository
) : SchemaValidator {

    constructor() : this(
        schemaRepository = JsonSchemaRepository(
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
            messageType = schemaType,
            version = version,
            schema = schema,
            jsonElement = jsonElement
        ).bind()

        json
    }

    private fun resolveSchema(
        messageType: SchemaType,
        version: Int
    ): Either<ValidationError, JsonSchema> =
        schemaRepository
            .get(messageType, version)
            .mapLeft { schemaError ->
                schemaError.toValidationError()
            }

    private fun SchemaError.toValidationError(): ValidationError =
        when (this) {
            is SchemaError.NotFound ->
                validationError(
                    messageType = messageType,
                    version = version,
                    "Schema resource not found: $messageType v$version"
                )

            is SchemaError.NoSchemasFound ->
                validationError(
                    messageType = messageType,
                    "No schemas found for message type: $messageType"
                )

            is SchemaError.InvalidSchema ->
                validationError(
                    messageType = messageType,
                    version = version,
                    "Invalid schema: $reason"
                )
        }

    private fun parseJson(
        messageType: SchemaType,
        payload: String
    ): Either<ValidationError, JsonElement> =
        Either.catch { Json.parseToJsonElement(payload) }
            .mapLeft { t ->
                validationError(
                    messageType = messageType,
                    "Invalid JSON: ${t.message}"
                )
            }

    private fun extractVersion(
        messageType: SchemaType,
        jsonElement: JsonElement
    ): Either<ValidationError, Int> = either {
        val version = jsonElement
            .jsonObject["version"]
            ?.jsonPrimitive
            ?.intOrNull

        ensureNotNull(version) {
            validationError(
                messageType = messageType,
                "Missing or invalid version field"
            )
        }
    }

    private fun validateAgainstSchema(
        messageType: SchemaType,
        version: Int,
        schema: JsonSchema,
        jsonElement: JsonElement
    ): Either<ValidationError, Unit> = either {
        val schemaErrors = mutableListOf<SchemaValidationError>()

        ensure(schema.validate(jsonElement, schemaErrors::add)) {
            ValidationError(
                messageType = messageType,
                version = version,
                errors = schemaErrors.map { it.message }
            )
        }
    }

    private fun validationError(
        messageType: SchemaType,
        version: Int,
        vararg errors: String
    ) = ValidationError(
        messageType = messageType,
        version = version,
        errors = errors.toList()
    )

    private fun validationError(
        messageType: SchemaType,
        vararg errors: String
    ) = ValidationError(
        messageType = messageType,
        version = null,
        errors = errors.toList()
    )
}
