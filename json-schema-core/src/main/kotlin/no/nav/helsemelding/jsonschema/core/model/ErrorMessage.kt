package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Description("An error message")
@Schema
@SchemaVersion(1)
@Serializable
data class ErrorMessage(
    val version: Int,
    val processedAt: String,
    val sourceSystem: String,
    val messageId: Uuid?,
    val errors: List<ProcessingError>,
    val originalMessage: OriginalMessage?
)

@Serializable
data class OriginalMessage(
    val publishedAt: String, // Serializer?
    val payload: String
)

@Serializable
data class ProcessingError(
    val category: ErrorCategory,
    val code: ErrorCode,
    val message: String
)

enum class ErrorCategory {
    VALIDATION,
    CONVERSION,
    SIGNING
}

enum class ErrorCode {
    INVALID_KAFKA_VALUE,
    MISSING_SOURCE_SYSTEM_HEADER,
    INVALID_MESSAGE,
    CONVERSION_ERROR,
    PDL_ERROR,
    PROVIDER_REGISTRY_ERROR,
    SIGNING_ERROR
}
