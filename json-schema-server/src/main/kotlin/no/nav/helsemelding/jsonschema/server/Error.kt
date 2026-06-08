package no.nav.helsemelding.jsonschema.server

import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaType

sealed interface SchemaServerError {
    data class Request(
        val error: RequestError
    ) : SchemaServerError

    data class Schema(
        val error: SchemaError
    ) : SchemaServerError

    data class NoSchemasFound(
        val schemaType: SchemaType
    ) : SchemaServerError
}

sealed interface RequestError {
    val message: String

    data object SchemaTypeMissing : RequestError {
        override val message = "Missing required path parameter: schemaType"
    }

    data object SchemaVersionMissing : RequestError {
        override val message = "Missing required path parameter: version"
    }

    data class UnknownSchemaType(
        val value: String
    ) : RequestError {
        override val message = "Unknown schema type: $value"
    }

    data class InvalidPathParameter(
        val name: String,
        val value: String,
        val expected: String
    ) : RequestError {
        override val message = "Invalid path parameter '$name': '$value'. Expected $expected."
    }
}
