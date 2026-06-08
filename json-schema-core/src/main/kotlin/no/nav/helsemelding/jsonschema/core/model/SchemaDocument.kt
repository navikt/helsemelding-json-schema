package no.nav.helsemelding.jsonschema.core.model

data class SchemaDocument(
    val schemaType: SchemaType,
    val version: Int,
    val schema: String
)
