package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
annotation class SchemaVersion(
    val value: Int
)

@Description("An outgoing dialog message")
@Schema
@SchemaVersion(1)
data class OutgoingDialogMessage(
    @Description("The current schema version") val version: Int,
    @Description("Unique identifier of the dialog message") val id: String,
    @Description("National identity number (11 digits) of the patient") val patientIdent: String,
    @Description("Reference id to a healthcare provider or provider office in the provider registry") val providerRef: String,
    @Description("Conversation this message belongs to") val conversationReference: ConversationReference?,
    @Description("Type of dialog message") val type: OutgoingDialogMessageType,
    @Description("Message text") val message: String?,
    @Description("Attachment encoded as a Base64 string") val attachment: String?
)
