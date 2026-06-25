package no.nav.helsemelding.jsonschema.core.loader

import io.github.optimumcode.json.schema.JsonSchema
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.FakeSchemaDocumentRepository

class SchemaLoaderSpec : StringSpec(
    {
        "should load existing schema" {
            val loader = schemaLoader(listOf(outgoingDialogMessageV1))

            loader.load(SchemaType.OUTGOING_DIALOG_MESSAGE, 1)
                .shouldBeRight()
                .shouldBeInstanceOf<JsonSchema>()
        }

        "should return left when schema document does not exist" {
            val repository = schemaLoader(emptyList())

            repository.load(SchemaType.OUTGOING_DIALOG_MESSAGE, 999) shouldBeLeft
                SchemaError.NotFound(
                    schemaType = SchemaType.OUTGOING_DIALOG_MESSAGE,
                    version = 999
                )
        }

        "should memoize loaded schemas" {
            val repository = schemaLoader(listOf(outgoingDialogMessageV1))

            val first = repository.load(SchemaType.OUTGOING_DIALOG_MESSAGE, 1).shouldBeRight()
            val second = repository.load(SchemaType.OUTGOING_DIALOG_MESSAGE, 1).shouldBeRight()

            first shouldBe second
        }
    }
)

private fun schemaLoader(documents: List<SchemaDocument>) =
    JsonSchemaLoader(
        schemaDocumentRepository = FakeSchemaDocumentRepository(
            documents = documents
        )
    )

private val outgoingDialogMessageV1 = SchemaDocument(
    schemaType = SchemaType.OUTGOING_DIALOG_MESSAGE,
    version = 1,
    schema = """
    {
      "${'$'}schema": "https://json-schema.org/draft/2020-12/schema",
      "type": "object"
    }
    """.trimIndent()
)
