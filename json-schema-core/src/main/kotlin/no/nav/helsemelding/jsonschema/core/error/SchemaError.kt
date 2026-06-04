package no.nav.helsemelding.jsonschema.core.error

import no.nav.helsemelding.jsonschema.core.model.SchemaType

sealed interface SchemaError {
    data class NotFound(
        val messageType: SchemaType,
        val version: Int
    ) : SchemaError

    data class NoSchemasFound(
        val messageType: SchemaType
    ) : SchemaError

    data class InvalidSchema(
        val messageType: SchemaType,
        val version: Int,
        val reason: String
    ) : SchemaError
}
