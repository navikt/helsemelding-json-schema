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
                    "patientPersonalId": "12345678910",
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "message": "Svar paa forespoersel",
                    "numberOfAttachments": 1,
                    "provider": {
                        "providerPersonalId": "12345678910",
                        "providerHprId": "987654321",
                        "providerOfficeOrgNr": "12345",
                        "providerOfficeHerId": "1234567",
                        "providerOfficeOrgName": "Testedal Legesenter"
                    },
                    "signature": {
                        "signingProviderPersonalId": "10987654321",
                        "signedAt": "2026-06-03T11:59:00Z"
                    },
                    "documentIdNote": "OD2503016631907"
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
                    "patientPersonalId": "12345678910",
                    "conversationReference": {
                        "parentMessageId": "parent-message-id",
                        "conversationId": "conversation-id"
                    },
                    "message": "Svar paa forespoersel",
                    "numberOfAttachments": 1,
                    "provider": {
                        "providerPersonalId": "12345678910",
                        "providerHprId": "987654321",
                        "providerOfficeOrgNr": "12345",
                        "providerOfficeHerId": "1234567",
                        "providerOfficeOrgName": "Testedal Legesenter"
                    },
                    "signature": {
                        "signingProviderPersonalId": "10987654321",
                        "signedAt": "2026-06-03T11:59:00Z"
                    },
                    "documentIdNote": "OD2503016631907"
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
                    "patientPersonalId": "12345678910",
                    "conversationReference": null,
                    "message": null,
                    "numberOfAttachments": 1,
                    "provider": {
                        "providerPersonalId": "12345678910",
                        "providerHprId": "987654321",
                        "providerOfficeOrgNr": "12345",
                        "providerOfficeHerId": "1234567",
                        "providerOfficeOrgName": "Testedal Legesenter"
                    },
                    "signature": {
                        "signingProviderPersonalId": "10987654321",
                        "signedAt": "2026-06-03T11:59:00Z"
                    },
                    "documentIdNote": "OD2503016631907"
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
    patientPersonalId = "12345678910",
    conversationReference = conversationReference,
    message = message,
    numberOfAttachments = 1,
    provider = Provider(
        providerPersonalId = "12345678910",
        providerHprId = "987654321",
        providerOfficeOrgNr = "12345",
        providerOfficeHerId = "1234567",
        providerOfficeOrgName = "Testedal Legesenter"
    ),
    signature = Signature(
        signingProviderPersonalId = "10987654321",
        signedAt = "2026-06-03T11:59:00Z"
    ),
    documentIdNote = "OD2503016631907"
)
