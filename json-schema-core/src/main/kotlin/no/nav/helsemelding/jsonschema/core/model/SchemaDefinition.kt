package no.nav.helsemelding.jsonschema.core.model

internal data class SchemaDefinition(
    val fileName: String,
    val version: Int,
    val schema: String
)
