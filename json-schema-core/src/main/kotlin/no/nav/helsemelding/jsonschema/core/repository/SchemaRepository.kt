package no.nav.helsemelding.jsonschema.core.repository

import arrow.core.Either
import arrow.core.memoize
import arrow.core.raise.catch
import arrow.core.raise.either
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
    ): Either<SchemaError, JsonSchema> = schema(messageType, version)

    private fun loadSchema(
        schemaType: SchemaType,
        version: Int
    ): Either<SchemaError, JsonSchema> = either {
        val document = schemaDocumentRepository
            .get(schemaType, version)
            .bind()

        catch(
            { JsonSchema.fromDefinition(document.content) },
            { t ->
                raise(
                    SchemaError.InvalidSchema(
                        schemaType = schemaType,
                        version = version,
                        reason = t.message ?: "Failed to load schema"
                    )
                )
            }
        )
    }
}
