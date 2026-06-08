package no.nav.helsemelding.jsonschema.server.model

import kotlinx.serialization.Serializable

@Serializable
data class SchemaMetadata(
    val schemaType: String,
    val version: Int
)
