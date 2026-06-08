package no.nav.helsemelding.jsonschema.core.error

import no.nav.helsemelding.jsonschema.core.model.SchemaType

sealed interface SchemaError {
    data class NotFound(
        val schemaType: SchemaType,
        val version: Int
    ) : SchemaError

    data class InvalidSchema(
        val schemaType: SchemaType,
        val version: Int,
        val reason: String
    ) : SchemaError
}
