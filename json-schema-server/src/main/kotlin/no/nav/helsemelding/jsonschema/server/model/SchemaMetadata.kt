package no.nav.helsemelding.jsonschema.server.model

import kotlinx.serialization.Serializable
import no.nav.helsemelding.jsonschema.core.model.SchemaType

@Serializable
data class SchemaMetadata(
    val schemaType: SchemaType,
    val version: Int
)
