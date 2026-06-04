package no.nav.helsemelding.jsonschema.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SchemaMetadata(
    val messageType: SchemaType,
    val version: Int
)
