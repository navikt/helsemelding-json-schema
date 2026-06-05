package no.nav.helsemelding.jsonschema.core.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.memoize
import io.github.optimumcode.json.schema.JsonSchema
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaType

interface SchemaRepository {
    fun get(messageType: SchemaType, version: Int): Either<SchemaError, JsonSchema>
}

class JsonSchemaRepository(
    private val schemaDocumentRepository: SchemaDocumentRepository
) : SchemaRepository {
    private val schema = ::loadSchema.memoize()

    override fun get(
        messageType: SchemaType,
        version: Int
    ): Either<SchemaError, JsonSchema> =
        schema(messageType, version)

    private fun loadSchema(
        messageType: SchemaType,
        version: Int
    ): Either<SchemaError, JsonSchema> =
        schemaDocumentRepository
            .get(messageType, version)
            .flatMap { document ->
                Either.catch { JsonSchema.fromDefinition(document.content) }
                    .mapLeft { throwable ->
                        SchemaError.InvalidSchema(
                            schemaType = messageType,
                            version = version,
                            reason = throwable.message ?: "Failed to load schema"
                        )
                    }
            }
}
