package no.nav.helsemelding.jsonschema.core.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

class OutgoingDialogMessageSpec : StringSpec(
    {
        "should serialize outgoing dialog message" {
            val message = outgoingDialogMessage()

            val encoded = Json.encodeToString(message)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "id": "86c61f31-44d4-47e8-a787-376297279505",
                    "patientIdent": "12345678910",
                    "providerId": "provider-id",
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "type": "MEETING_INVITATION_2",
                    "message": "Innkalling til dialogmoete",
                    "attachment": "base64-attachment"
                }
                """.trimIndent()
            )
        }

        "should deserialize outgoing dialog message" {
            val decoded = Json.decodeFromString<OutgoingDialogMessage>(
                """
                {
                    "version": 1,
                    "id": "86c61f31-44d4-47e8-a787-376297279505",
                    "patientIdent": "12345678910",
                    "providerId": "provider-id",
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "type": "MEETING_INVITATION_2",
                    "message": "Innkalling til dialogmoete",
                    "attachment": "base64-attachment"
                }
                """.trimIndent()
            )

            decoded shouldBe outgoingDialogMessage()
        }

        "should serialize nullable outgoing dialog message fields as explicit nulls" {
            val message = outgoingDialogMessage(
                conversationReference = null,
                message = null,
                attachment = null
            )

            val encoded = Json.encodeToString(message)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "id": "86c61f31-44d4-47e8-a787-376297279505",
                    "patientIdent": "12345678910",
                    "providerId": "provider-id",
                    "conversationReference": null,
                    "type": "MEETING_INVITATION_2",
                    "message": null,
                    "attachment": null
                }
                """.trimIndent()
            )
        }

        "should reject outgoing dialog message with invalid UUID id" {
            shouldThrow<IllegalArgumentException> {
                Json.decodeFromString<OutgoingDialogMessage>(
                    """{"version":1,"id":"dialog-1","patientIdent":"12345678910","providerId":"provider-id","conversationReference":null,"type":"NAV_MESSAGE","message":null,"attachment":null}"""
                )
            }
        }
    }
)

private fun outgoingDialogMessage(
    conversationReference: ConversationReference? = ConversationReference(
        parentMessageId = "parent-message-id",
        conversationId = "conversation-id"
    ),
    message: String? = "Innkalling til dialogmoete",
    attachment: String? = "base64-attachment"
) = OutgoingDialogMessage(
    version = 1,
    id = Uuid.parse("86c61f31-44d4-47e8-a787-376297279505"),
    patientIdent = "12345678910",
    providerId = "provider-id",
    conversationReference = conversationReference,
    type = OutgoingDialogMessageType.MEETING_INVITATION_2,
    message = message,
    attachment = attachment
)
