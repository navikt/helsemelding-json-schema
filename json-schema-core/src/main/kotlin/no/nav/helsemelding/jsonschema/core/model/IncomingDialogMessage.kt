package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema

@Description("An incoming dialog message")
@Schema
@SchemaVersion(1)
data class IncomingDialogMessage(
    @Description("The current schema version") val version: Int,
    @Description("The id of the dialog message") val id: String,
    @Description("Type of message that was received") val type: IncomingDialogMessageType,
    @Description("When the dialog message was received (UTC)") val receivedAt: String,
    @Description("National identity number (11 digits) of patient") val patientIdent: String,
    @Description("Who the dialog message is from") val sender: Sender,
    @Description("") val conversationRef: ConversationRef?,
    @Description("The actual dialog message") val message: String?,
    @Description("Number of attachment in message") val numberOfAttachments: Int
)

@Description("Information about the sender")
@Schema
data class Sender(
    @Description("Reference id in behandlerregisteret to behandler") val behandlerRef: String,
    @Description("Reference id in behandlerregisteret to behandler who signed the dialog message") val legeSignaturRef: String
)
