package no.nav.helsemelding.jsonschema.server.plugin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.FakeSchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.service.JsonSchemaService

class RoutesSpec : StringSpec(
    {
        "should list all schemas" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas")

                response.status shouldBe HttpStatusCode.OK
            }
        }

        "should list versions for schema type" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/dialog-message")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe "[1,2]"
            }
        }

        "should return latest schema" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/dialog-message/latest")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"dialog-message-v2"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return schema by version" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/dialog-message/v1")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"dialog-message-v1"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return 404 for unknown version" {
            withSchemaRoutes {
                client.get("/api/v1/schemas/dialog-message/v999").status shouldBe HttpStatusCode.NotFound
            }
        }

        "should return 400 for unknown schema type" {
            withSchemaRoutes {
                client.get("/api/v1/schemas/unknown-message/v1").status shouldBe HttpStatusCode.BadRequest
            }
        }

        "should return 400 for invalid version" {
            withSchemaRoutes {
                client.get("/api/v1/schemas/dialog-message/vabc").status shouldBe HttpStatusCode.BadRequest
            }
        }
    }
)

private fun withSchemaRoutes(
    testBuilder: suspend ApplicationTestBuilder.() -> Unit
) = testApplication {
    install(ContentNegotiation) {
        json()
    }
    application {
        routing { externalRoutes(jsonSchemaService()) }
    }
    testBuilder()
}

private fun jsonSchemaService() = JsonSchemaService(
    FakeSchemaDocumentRepository(
        listOf(
            schemaDocument(1),
            schemaDocument(2)
        )
    )
)

private fun schemaDocument(version: Int) = SchemaDocument(
    SchemaType.DIALOG_MESSAGE,
    version,
    """{"title":"dialog-message-v$version"}"""
)
