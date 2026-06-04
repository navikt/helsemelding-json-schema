package no.nav.helsemelding.jsonschema.core.repository

import io.github.optimumcode.json.schema.JsonSchema
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType

class SchemaRepositorySpec : StringSpec(
    {
        "should load existing schema from document repository" {
            val repository = schemaRepository(listOf(dialogMessageV1))

            repository.get(SchemaType.DIALOG_MESSAGE, 1)
                .shouldBeRight()
                .shouldBeInstanceOf<JsonSchema>()
        }

        "should return left when schema document does not exist" {
            val repository = schemaRepository(emptyList())

            repository.get(SchemaType.DIALOG_MESSAGE, 999) shouldBeLeft
                SchemaError.NotFound(
                    messageType = SchemaType.DIALOG_MESSAGE,
                    version = 999
                )
        }

        "should memoize loaded schemas" {
            val repository = schemaRepository(listOf(dialogMessageV1))

            val first = repository.get(SchemaType.DIALOG_MESSAGE, 1).shouldBeRight()
            val second = repository.get(SchemaType.DIALOG_MESSAGE, 1).shouldBeRight()

            first shouldBe second
        }
    }
)

private fun schemaRepository(documents: List<SchemaDocument>) =
    JsonSchemaRepository(
        schemaDocumentRepository = FakeSchemaDocumentRepository(
            documents = documents
        )
    )

private val dialogMessageV1 = SchemaDocument(
    messageType = SchemaType.DIALOG_MESSAGE,
    version = 1,
    content = """
    {
      "${'$'}schema": "https://json-schema.org/draft/2020-12/schema",
      "type": "object"
    }
    """.trimIndent()
)
