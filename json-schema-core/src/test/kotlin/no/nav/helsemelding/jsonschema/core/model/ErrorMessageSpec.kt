package no.nav.helsemelding.jsonschema.core.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

class ErrorMessageSpec : StringSpec(
    {
        "should serialize error message" {
            val errorMessage = errorMessage()

            val encoded = Json.encodeToString(errorMessage)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "processedAt": "2026-06-03T12:00:00Z",
                    "sourceSystem": "some-system",
                    "messageId": "86c61f31-44d4-47e8-a787-376297279505",
                    "errors": [
                        {
                            "category": "VALIDATION",
                            "code": "INVALID_MESSAGE",
                            "message": "Invalid message"
                        }
                    ],
                    "originalMessage": {
                        "publishedAt": "2026-06-03T11:00:00Z",
                        "payload": "{\"message\":\"valid\"}"
                    }
                }
                """.trimIndent()
            )
        }

        "should deserialize error message" {
            val decoded = Json.decodeFromString<ErrorMessage>(
                """
                {
                    "version": 1,
                    "processedAt": "2026-06-03T12:00:00Z",
                    "sourceSystem": "some-system",
                    "messageId": "86c61f31-44d4-47e8-a787-376297279505",
                    "errors": [
                        {
                            "category": "VALIDATION",
                            "code": "INVALID_MESSAGE",
                            "message": "Invalid message"
                        }
                    ],
                    "originalMessage": {
                        "publishedAt": "2026-06-03T11:00:00Z",
                        "payload": "{\"message\":\"valid\"}"
                    }
                }
                """.trimIndent()
            )

            decoded shouldBe errorMessage()
        }

        "should serialize nullable error message fields as explicit nulls" {
            val errorMessage = errorMessage(
                messageId = null,
                originalMessage = null
            )

            val encoded = Json.encodeToString(errorMessage)

            Json.parseToJsonElement(encoded) shouldBe Json.parseToJsonElement(
                """
                {
                    "version": 1,
                    "processedAt": "2026-06-03T12:00:00Z",
                    "sourceSystem": "some-system",
                    "messageId": null,
                    "errors": [
                        {
                            "category": "VALIDATION",
                            "code": "INVALID_MESSAGE",
                            "message": "Invalid message"
                        }
                    ],
                    "originalMessage": null
                }
                """.trimIndent()
            )
        }
    }
)

private fun errorMessage(
    messageId: Uuid? = Uuid.parse("86c61f31-44d4-47e8-a787-376297279505"),
    originalMessage: OriginalMessage? = OriginalMessage(
        publishedAt = "2026-06-03T11:00:00Z",
        payload = """{"message":"valid"}"""
    )
) = ErrorMessage(
    version = 1,
    processedAt = "2026-06-03T12:00:00Z",
    sourceSystem = "some-system",
    messageId = messageId,
    errors = listOf(
        ProcessingError(
            category = ErrorCategory.VALIDATION,
            code = ErrorCode.INVALID_MESSAGE,
            message = "Invalid message"
        )
    ),
    originalMessage = originalMessage
)
