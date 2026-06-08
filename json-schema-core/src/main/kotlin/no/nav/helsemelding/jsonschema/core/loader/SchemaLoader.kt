package no.nav.helsemelding.jsonschema.core.loader

import arrow.core.Either
import arrow.core.memoize
import arrow.core.raise.catch
import arrow.core.raise.either
import io.github.optimumcode.json.schema.JsonSchema
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.SchemaDocumentRepository

interface SchemaLoader {
    fun load(schemaType: SchemaType, version: Int): Either<SchemaError, JsonSchema>
}

class JsonSchemaLoader(
    private val schemaDocumentRepository: SchemaDocumentRepository
) : SchemaLoader {
    private val schema = ::loadSchema.memoize()

    override fun load(
        schemaType: SchemaType,
        version: Int
    ): Either<SchemaError, JsonSchema> = schema(schemaType, version)

    private fun loadSchema(
        schemaType: SchemaType,
        version: Int
    ): Either<SchemaError, JsonSchema> = either {
        val document = schemaDocumentRepository
            .get(schemaType, version)
            .bind()

        catch(
            { JsonSchema.fromDefinition(document.schema) },
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
