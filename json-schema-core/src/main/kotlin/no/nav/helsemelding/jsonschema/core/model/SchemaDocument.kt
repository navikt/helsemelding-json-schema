package no.nav.helsemelding.jsonschema.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SchemaDocument(
    val messageType: SchemaType,
    val version: Int,
    val content: String
)
