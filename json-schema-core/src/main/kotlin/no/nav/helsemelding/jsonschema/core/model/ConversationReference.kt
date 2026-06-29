package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlinx.serialization.Serializable

@Description("Information about the conversation")
@Schema
@Serializable
data class ConversationReference(
    @Description("Reference to previous dialog message in conversation") val parentMessageId: String,
    @Description("Reference to conversation (typically the same id as initial dialog message)") val conversationId: String
)
