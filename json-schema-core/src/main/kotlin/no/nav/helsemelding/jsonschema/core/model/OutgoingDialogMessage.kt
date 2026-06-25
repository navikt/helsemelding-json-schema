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
    @Description("The id of the dialog message") val id: String,
    @Description("National identity number (11 digits) of patient") val patientIdent: String,
    @Description("Reference id to behandler or behandlerkontor in behandlerregisteret") val behandlerRef: String,
    @Description("Which conversation this message is part of") val conversationRef: ConversationRef?,
    @Description("Type of message to send") val type: OutgoingDialogMessageType,
    @Description("The actual dialog message") val message: String?,
    @Description("Attachment as base64 encoded string?") val attachment: String?
)

@Description("Information about the conversation")
@Schema
data class ConversationRef(
    @Description("Reference to previous dialog message in conversation") val refToParent: String,
    @Description("Reference to conversation (typically the same id as initial dialog message)") val refToConversation: String
)
