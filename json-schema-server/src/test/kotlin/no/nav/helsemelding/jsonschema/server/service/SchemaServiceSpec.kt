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
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.FakeSchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.SchemaServerError
import no.nav.helsemelding.jsonschema.server.model.SchemaMetadata

class SchemaServiceSpec : StringSpec(
    {
        val outgoingDialogMessageV1 = SchemaDocument(
            schemaType = SchemaType.OUTGOING_DIALOG_MESSAGE,
            version = 1,
            schema = """{"title":"outgoing-dialog-message-v1"}"""
        )

        val outgoingDialogMessageV2 = SchemaDocument(
            schemaType = SchemaType.OUTGOING_DIALOG_MESSAGE,
            version = 2,
            schema = """{"title":"outgoing-dialog-message-v2"}"""
        )

        val incomingDialogMessageV1 = SchemaDocument(
            schemaType = SchemaType.INCOMING_DIALOG_MESSAGE,
            version = 1,
            schema = """{"title":"incoming-dialog-message-v1"}"""
        )

        val schemaRepository = FakeSchemaDocumentRepository(
            listOf(
                outgoingDialogMessageV2,
                outgoingDialogMessageV1,
                incomingDialogMessageV1
            )
        )
        val schemaService = JsonSchemaService(schemaRepository)

        "should get schemas sorted by schema type and version" {
            schemaService.getSchemas() shouldContainExactly listOf(
                SchemaMetadata(SchemaType.INCOMING_DIALOG_MESSAGE.toString(), 1),
                SchemaMetadata(SchemaType.OUTGOING_DIALOG_MESSAGE.toString(), 1),
                SchemaMetadata(SchemaType.OUTGOING_DIALOG_MESSAGE.toString(), 2)
            )
        }

        "should get versions for schema type sorted ascending" {
            schemaService.getVersions(SchemaType.OUTGOING_DIALOG_MESSAGE) shouldBe listOf(1, 2)
        }

        "should get schema by schema type and version" {
            either {
                with(schemaService) {
                    get(SchemaType.OUTGOING_DIALOG_MESSAGE, 1)
                }
            }
                .shouldBeRight(outgoingDialogMessageV1)
        }

        "should return error when schema version does not exist" {
            val error = either {
                with(schemaService) {
                    get(SchemaType.OUTGOING_DIALOG_MESSAGE, 999)
                }
            }
                .shouldBeLeft()

            val schemaError = error.shouldBeInstanceOf<SchemaServerError.Schema>().error

            schemaError.shouldBeInstanceOf<SchemaError.NotFound>()
            schemaError.schemaType shouldBe SchemaType.OUTGOING_DIALOG_MESSAGE
            schemaError.version shouldBe 999
        }

        "should return latest schema by highest version" {
            either {
                with(schemaService) {
                    getLatest(SchemaType.OUTGOING_DIALOG_MESSAGE)
                }
            }
                .shouldBeRight(outgoingDialogMessageV2)
        }

        "should return error when latest schema does not exist" {
            val emptyService = JsonSchemaService(
                FakeSchemaDocumentRepository(emptyList())
            )

            val error = either {
                with(emptyService) {
                    getLatest(SchemaType.OUTGOING_DIALOG_MESSAGE)
                }
            }
                .shouldBeLeft()

            val schemaError = error.shouldBeInstanceOf<SchemaServerError.NoSchemasFound>()

            schemaError.schemaType shouldBe SchemaType.OUTGOING_DIALOG_MESSAGE
        }

        "should expose schema content" {
            val schema = either {
                with(schemaService) {
                    getLatest(SchemaType.OUTGOING_DIALOG_MESSAGE)
                }
            }
                .shouldBeRight()

            schema.schema shouldBe """{"title":"outgoing-dialog-message-v2"}"""
        }
    }
)
