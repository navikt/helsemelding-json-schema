package no.nav.helsemelding.jsonschema.server

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)
