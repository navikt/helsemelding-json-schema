package no.nav.helsemelding.jsonschema.core.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class IncomingDialogMessageSpec : StringSpec(
    {
        "should serialize incoming dialog message" {
            val message = incomingDialogMessage()

            val encoded = Json.encodeToString(message)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "id": "incoming-dialog-message-id",
                    "type": "PATIENT_REQUEST_RESPONSE",
                    "receivedAt": "2026-06-03T12:00:00Z",
                    "patientIdent": "12345678910",
                    "sender": {
                        "providerId": "provider-id",
                        "signingProviderId": "signing-provider-id"
                    },
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "message": "Svar paa forespoersel",
                    "numberOfAttachments": 1
                }
                """.trimIndent()
            )
        }

        "should deserialize incoming dialog message" {
            val decoded = Json.decodeFromString<IncomingDialogMessage>(
                """
                {
                    "version": 1,
                    "id": "incoming-dialog-message-id",
                    "type": "PATIENT_REQUEST_RESPONSE",
                    "receivedAt": "2026-06-03T12:00:00Z",
                    "patientIdent": "12345678910",
                    "sender": {
                        "providerId": "provider-id",
                        "signingProviderId": "signing-provider-id"
                    },
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "message": "Svar paa forespoersel",
                    "numberOfAttachments": 1
                }
                """.trimIndent()
            )

            decoded shouldBe incomingDialogMessage()
        }

        "should serialize nullable incoming dialog message fields as explicit nulls" {
            val message = incomingDialogMessage(
                conversationReference = null,
                message = null
            )

            val encoded = Json.encodeToString(message)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "id": "incoming-dialog-message-id",
                    "type": "PATIENT_REQUEST_RESPONSE",
                    "receivedAt": "2026-06-03T12:00:00Z",
                    "patientIdent": "12345678910",
                    "sender": {
                        "providerId": "provider-id",
                        "signingProviderId": "signing-provider-id"
                    },
                    "conversationReference": null,
                    "message": null,
                    "numberOfAttachments": 1
                }
                """.trimIndent()
            )
        }
    }
)

private fun incomingDialogMessage(
    conversationReference: ConversationReference? = ConversationReference(
        parentMessageId = "parent-message-id",
        conversationId = "conversation-id"
    ),
    message: String? = "Svar paa forespoersel"
) = IncomingDialogMessage(
    version = 1,
    id = "incoming-dialog-message-id",
    type = IncomingDialogMessageType.PATIENT_REQUEST_RESPONSE,
    receivedAt = "2026-06-03T12:00:00Z",
    patientIdent = "12345678910",
    sender = Sender(
        providerId = "provider-id",
        signingProviderId = "signing-provider-id"
    ),
    conversationReference = conversationReference,
    message = message,
    numberOfAttachments = 1
)
