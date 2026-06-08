package no.nav.helsemelding.jsonschema.server

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import io.ktor.server.application.ApplicationCall
import no.nav.helsemelding.jsonschema.core.model.SchemaType

private const val SCHEMA_TYPE = "schemaType"
private const val VERSION = "version"

fun Raise<SchemaServerError>.schemaType(call: ApplicationCall): SchemaType {
    val value = requiredPathParam(
        call = call,
        name = SCHEMA_TYPE,
        missingError = RequestError.SchemaTypeMissing
    )

    return SchemaType.from(value)
        ?: raise(
            SchemaServerError.Request(
                RequestError.UnknownSchemaType(value)
            )
        )
}

fun Raise<SchemaServerError>.version(call: ApplicationCall): Int {
    val value = requiredPathParam(
        call = call,
        name = VERSION,
        missingError = RequestError.SchemaVersionMissing
    )

    return value.toIntOrNull()
        ?: raise(
            SchemaServerError.Request(
                RequestError.InvalidPathParameter(
                    name = VERSION,
                    value = value,
                    expected = "integer"
                )
            )
        )
}

private fun Raise<SchemaServerError>.requiredPathParam(
    call: ApplicationCall,
    name: String,
    missingError: RequestError
): String {
    val value = call.parameters[name]?.trim()

    return ensureNotNull(value) {
        SchemaServerError.Request(missingError)
    }
        .also {
            ensure(it.isNotBlank()) {
                SchemaServerError.Request(missingError)
            }
        }
}
