package no.nav.helsemelding.jsonschema.core.validation

import no.nav.helsemelding.jsonschema.core.model.SchemaType

data class ValidationError(
    val schemaType: SchemaType,
    val version: Int?,
    val errors: List<String>
)
