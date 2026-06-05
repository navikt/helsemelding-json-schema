package no.nav.helsemelding.jsonschema.core.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType

class SchemaDocumentRepositorySpec : StringSpec(
    {
        "should load schemas from resources" {
            val documents = JsonSchemaDocumentRepository().list()

            documents shouldHaveSize 1

            documents shouldContain SchemaDocument(
                schemaType = SchemaType.DIALOG_MESSAGE,
                version = 1,
                schema = documents.first().schema
            )
        }

        "should get dialog-message v1 schema" {
            val document = JsonSchemaDocumentRepository()
                .get(SchemaType.DIALOG_MESSAGE, 1)
                .shouldBeRight()

            document.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            document.version shouldBe 1
            document.schema.contains("\"${'$'}schema\"") shouldBe true
            document.schema.contains("\"type\": \"object\"") shouldBe true
        }

        "should get latest dialog-message schema" {
            val document = JsonSchemaDocumentRepository()
                .latest(SchemaType.DIALOG_MESSAGE)
                .shouldBeRight()

            document.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            document.version shouldBe 1
            document.schema.contains("\"${'$'}schema\"") shouldBe true
            document.schema.contains("\"type\": \"object\"") shouldBe true
        }

        "should return error when schema does not exist" {
            val error = JsonSchemaDocumentRepository()
                .get(SchemaType.DIALOG_MESSAGE, 999)
                .shouldBeLeft()

            error.shouldBeInstanceOf<SchemaError.NotFound>()
            error.schemaType shouldBe SchemaType.DIALOG_MESSAGE
            error.version shouldBe 999
        }

        "should return error when no schemas exist for message type" {
            val error = FakeSchemaDocumentRepository(emptyList())
                .latest(SchemaType.DIALOG_MESSAGE)
                .shouldBeLeft()

            error.shouldBeInstanceOf<SchemaError.NoSchemasFound>()
            error.schemaType shouldBe SchemaType.DIALOG_MESSAGE
        }

        "should sort schemas by message type and version" {
            val documents = JsonSchemaDocumentRepository().list()

            documents shouldBe documents.sortedWith(
                compareBy(
                    { it.schemaType },
                    { it.version }
                )
            )
        }
    }
)
