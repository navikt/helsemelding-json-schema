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

        "should accept valid dialog-message json" {
            val json = """
            {
              "version": 1,
              "dialogId": "dialog-123",
              "createdAt": "2026-06-03T12:00:00Z",
              "payload": {
                "messageText": "Hei",
                "patientId": "12345678910"
              }
            }
            """.trimIndent()

            validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeRight(json)
        }

        "should reject invalid json syntax" {
            val json = """
            {
              "version": 1,
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            error.version shouldBe null
            error.errors.first().startsWith("Invalid JSON") shouldBe true
        }

        "should reject missing required field" {
            val json = """
            {
              "version": 1,
              "createdAt": "2026-06-03T12:00:00Z",
              "payload": {
                "messageText": "Hei",
                "patientId": "12345678910"
              }
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
            error.errors.any { it.contains("dialogId") } shouldBe true
        }

        "should reject additional properties" {
            val json = """
            {
              "version": 1,
              "dialogId": "dialog-123",
              "createdAt": "2026-06-03T12:00:00Z",
              "unexpected": "not allowed",
              "payload": {
                "messageText": "Hei",
                "patientId": "12345678910"
              }
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeLeft()

            error.version shouldBe 1
        }

        "should reject unknown schema version" {
            val json = """
            {
              "version": 999
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            error.version shouldBe 999
            error.errors shouldContain "Schema resource not found: dialog-message v999"
        }

        "should reject missing version field" {
            val json = """
            {
              "dialogId": "dialog-123",
              "createdAt": "2026-06-03T12:00:00Z",
              "payload": {
                "messageText": "Hei",
                "patientId": "12345678910"
              }
            }
            """.trimIndent()

            val error = validator.validate(SchemaType.DIALOG_MESSAGE, json).shouldBeLeft()

            error.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            error.version shouldBe null
            error.errors shouldContain "Missing or invalid version field"
        }
    }
)
