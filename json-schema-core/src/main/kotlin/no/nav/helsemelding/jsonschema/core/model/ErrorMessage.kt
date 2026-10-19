package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Description("An error when message couldn't be delivered")
@Schema
@SchemaVersion(1)
@Serializable
data class ErrorMessage(
    @Description("The current schema version")
    val version: Int,

    @Description("Date and time the dialog message was processed (UTC)")
    val processedAt: Instant,

    @Description("Value of the sourceSystem header from the rejected record")
    val sourceSystem: String,

    @Description("Unique identifier of the dialog message")
    val messageId: Uuid?,

    @Description("List of possible errors when processing the message")
    val errors: List<ProcessingError>,

    @Description("Information about the original message")
    val originalMessage: OriginalMessage?
)

@Description("Information about the original message")
@Schema
@Serializable
data class OriginalMessage(
    @Description("Date and time the dialog message was published (UTC)")
    val publishedAt: Instant,

    @Description("Original record (JSON) published to helsemelding.dialog.out")
    val payload: String
)

@Description("Error when processing a message")
@Schema
@Serializable
data class ProcessingError(
    @Description("Error category")
    val category: ErrorCategory,

    @Description("Machine-readable error code")
    val code: ErrorCode,

    @Description("Human-readable error description")
    val message: String
)

@Serializable
enum class ErrorCategory {
    VALIDATION,
    CONVERSION,
    SIGNING
}

@Serializable
enum class ErrorCode {
    INVALID_KAFKA_VALUE,
    MISSING_SOURCE_SYSTEM_HEADER,
    INVALID_MESSAGE,
    CONVERSION_ERROR,
    MESSAGE_ID_EXTRACTION_ERROR,
    PDL_ERROR,
    PROVIDER_REGISTRY_ERROR,
    SIGNING_ERROR
}
