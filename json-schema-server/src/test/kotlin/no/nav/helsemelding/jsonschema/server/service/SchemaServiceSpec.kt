package no.nav.helsemelding.jsonschema.server.service

import arrow.core.raise.either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaMetadata
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.FakeSchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.SchemaServerError

class SchemaServiceSpec : StringSpec(
    {
        val dialogMessageV1 = SchemaDocument(
            messageType = SchemaType.DIALOG_MESSAGE,
            version = 1,
            content = """{"title":"dialog-message-v1"}"""
        )

        val dialogMessageV2 = SchemaDocument(
            messageType = SchemaType.DIALOG_MESSAGE,
            version = 2,
            content = """{"title":"dialog-message-v2"}"""
        )

        val schemaRepository = FakeSchemaDocumentRepository(
            listOf(
                dialogMessageV2,
                dialogMessageV1
            )
        )
        val schemaService = JsonSchemaService(schemaRepository)

        "should list schemas sorted by message type and version" {
            schemaService.listSchemas() shouldContainExactly listOf(
                SchemaMetadata(SchemaType.DIALOG_MESSAGE, 1),
                SchemaMetadata(SchemaType.DIALOG_MESSAGE, 2)
            )
        }

        "should list versions for message type sorted ascending" {
            schemaService.listVersions(SchemaType.DIALOG_MESSAGE) shouldBe listOf(1, 2)
        }

        "should get schema by message type and version" {
            either {
                with(schemaService) {
                    get(SchemaType.DIALOG_MESSAGE, 1)
                }
            }
                .shouldBeRight(dialogMessageV1)
        }

        "should return error when schema version does not exist" {
            val error = either {
                with(schemaService) {
                    get(SchemaType.DIALOG_MESSAGE, 999)
                }
            }
                .shouldBeLeft()

            val schemaError = error.shouldBeInstanceOf<SchemaServerError.Schema>().error

            schemaError.shouldBeInstanceOf<SchemaError.NotFound>()
            schemaError.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            schemaError.version shouldBe 999
        }

        "should return latest schema by highest version" {
            either {
                with(schemaService) {
                    latest(SchemaType.DIALOG_MESSAGE)
                }
            }
                .shouldBeRight(dialogMessageV2)
        }

        "should return error when latest schema does not exist" {
            val emptyService = JsonSchemaService(
                FakeSchemaDocumentRepository(emptyList())
            )

            val error = either {
                with(emptyService) {
                    latest(SchemaType.DIALOG_MESSAGE)
                }
            }
                .shouldBeLeft()

            val schemaError = error.shouldBeInstanceOf<SchemaServerError.Schema>().error

            schemaError.shouldBeInstanceOf<SchemaError.NoSchemasFound>()
            schemaError.schemaType shouldBe SchemaType.DIALOG_MESSAGE
        }

        "should expose schema content" {
            val schema = either {
                with(schemaService) {
                    latest(SchemaType.DIALOG_MESSAGE)
                }
            }
                .shouldBeRight()

            schema.content shouldBe """{"title":"dialog-message-v2"}"""
        }
    }
)
