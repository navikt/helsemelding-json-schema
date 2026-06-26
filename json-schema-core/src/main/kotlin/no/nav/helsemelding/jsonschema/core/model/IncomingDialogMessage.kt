package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema

@Description("An incoming dialog message")
@Schema
@SchemaVersion(1)
data class IncomingDialogMessage(
    @Description("The current schema version") val version: Int,
    @Description("Unique identifier of the dialog message") val id: String,
    @Description("Type of dialog message") val type: IncomingDialogMessageType,
    @Description("Date and time the dialog message was received (UTC)") val receivedAt: String,
    @Description("National identity number (11 digits) of the patient") val patientIdent: String,
    @Description("Sender of the dialog message") val sender: Sender,
    @Description("Conversation this message belongs to") val conversationReference: ConversationReference?,
    @Description("Message text") val message: String?,
    @Description("Number of attachments") val numberOfAttachments: Int
)

@Description("Information about the sender")
@Schema
data class Sender(
    @Description("Reference id in the provider registry for the healthcare provider") val providerId: String,
    @Description("Reference id in the provider registry for the healthcare provider who signed the message") val signingProviderId: String
)
