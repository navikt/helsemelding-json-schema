package no.nav.helsemelding.jsonschema.core.validation

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import no.nav.helsemelding.jsonschema.core.model.SchemaType

class SchemaValidatorSpec : StringSpec(
    {
        val validator = JsonSchemaValidator()

        "should accept valid outgoing-dialog-message json" {
            val json = """
            {
                "version": 1,
                "id": "uuid",
                "patientIdent": "12345678910",
                "providerId": "uuid2",
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "type": "MEETING_INVITATION_2",
                "message": "Hei",
                "attachment": "attachment"
            }
            """.trimIndent()

            validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeRight(json)
        }

        "should reject invalid json syntax" {
            val json = """
            {
              "version": 1,
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.OUTGOING_DIALOG_MESSAGE
            error.version shouldBe null
            error.errors.first().startsWith("Invalid JSON") shouldBe true
        }

        "should reject missing required field for outgoing-dialog-message" {
            val json = """
            {
                "version": 1,
                "patientIdent": "12345678910",
                "providerId": "uuid2",
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "type": "MEETING_INVITATION_2",
                "message": "Hei",
                "attachment": "attachment"
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
            error.errors.any { it.contains("id") } shouldBe true
        }

        "should reject additional properties for outgoing-dialog-message" {
            val json = """
            {
                "version": 1,
                "id": "uuid",
                "patientIdent": "12345678910",
                "providerId": "uuid2",
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "type": "MEETING_INVITATION_2",
                "message": "Hei",
                "unexpected": "not allowed",
                "attachment": "attachment"
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
        }

        "should reject unknown schema version" {
            val json = """
            {
              "version": 999
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.OUTGOING_DIALOG_MESSAGE
            error.version shouldBe 999
            error.errors shouldContain "Schema resource not found: outgoing-dialog-message v999"
        }

        "should reject missing version field for outgoing-dialog-message" {
            val json = """
            {
                "id": "uuid",
                "patientIdent": "12345678910",
                "providerId": "uuid2",
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "type": "MEETING_INVITATION_2",
                "message": "Hei",
                "attachment": "attachment"
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.OUTGOING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.OUTGOING_DIALOG_MESSAGE
            error.version shouldBe null
            error.errors shouldContain "Missing or invalid version field"
        }

        "should accept valid incoming-dialog-message json" {
            val json = """
            {
                "version": 1,
                "id": "uuid",
                "type": "PATIENT_REQUEST_RESPONSE",
                "receivedAt": "2026-06-03T12:00:00Z",
                "patientIdent": "12345678910",
                "sender": {
                    "providerId": "uuid2",
                    "signingProviderId": "uuid3"
                },
                "conversationReference": {
                    "parentMessageId": "uuid4",
                    "conversationId": "uuid5"
                },
                "message": "Hei",
                "numberOfAttachments": 1
            }
            """.trimIndent()

            validator.validate(SchemaType.INCOMING_DIALOG_MESSAGE, json).shouldBeRight(json)
        }

        "should reject missing required field for incoming-dialog-message" {
            val json = """
            {
                "version": 1,
                "type": "PATIENT_REQUEST_RESPONSE",
                "receivedAt": "2026-06-03T12:00:00Z",
                "patientIdent": "12345678910",
                "sender": {
                    "providerId": "uuid",
                    "signingProviderId": "uuid2"
                },
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "message": "Hei",
                "numberOfAttachments": 1
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.INCOMING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
            error.errors.any { it.contains("id") } shouldBe true
        }

        "should reject additional properties for incoming-dialog-message" {
            val json = """
            {
                "version": 1,
                "type": "PATIENT_REQUEST_RESPONSE",
                "receivedAt": "2026-06-03T12:00:00Z",
                "patientIdent": "12345678910",
                "sender": {
                    "providerId": "uuid",
                    "signingProviderId": "uuid2"
                },
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "message": "Hei",
                "unexpected": "not allowed",
                "numberOfAttachments": 1
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.INCOMING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
        }

        "should reject missing version field for incoming-dialog-message" {
            val json = """
            {
                "type": "PATIENT_REQUEST_RESPONSE",
                "receivedAt": "2026-06-03T12:00:00Z",
                "patientIdent": "12345678910",
                "sender": {
                    "providerId": "uuid",
                    "signingProviderId": "uuid2"
                },
                "conversationReference": {
                    "parentMessageId": "uuid3",
                    "conversationId": "uuid4"
                },
                "message": "Hei",
                "numberOfAttachments": 1
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.INCOMING_DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.INCOMING_DIALOG_MESSAGE
            error.version shouldBe null
            error.errors shouldContain "Missing or invalid version field"
        }
    }
)
