package no.nav.helsemelding.jsonschema.server.plugin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.HttpClient
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
import no.nav.helsemelding.jsonschema.server.model.ErrorResponse
import no.nav.helsemelding.jsonschema.server.service.JsonSchemaService
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

class RoutesSpec : StringSpec(
    {
        "should get all schemas" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldContain SchemaType.OUTGOING_DIALOG_MESSAGE.toString()
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should get versions for outgoing-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/outgoing-dialog-message")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe "[1,2]"
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return latest schema for outgoing-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/outgoing-dialog-message/latest")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"outgoing-dialog-message-v2"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return schema by version for outgoing-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/outgoing-dialog-message/v1")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"outgoing-dialog-message-v1"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return 404 for unknown version of outgoing-dialog-message" {
            withSchemaRoutes {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/outgoing-dialog-message/v999")

                response.status shouldBe HttpStatusCode.NotFound
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "Schema not found: outgoing-dialog-message v999"
                )
            }
        }

        "should return 400 for unknown schema type" {
            withSchemaRoutes {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/unknown-message/v1")

                response.status shouldBe HttpStatusCode.BadRequest
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "Unknown schema type: unknown-message"
                )
            }
        }

        "should return 400 for invalid version of outgoing-dialog-message" {
            withSchemaRoutes {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/outgoing-dialog-message/vabc")

                response.status shouldBe HttpStatusCode.BadRequest
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "Invalid path parameter 'version': 'abc'. Expected integer."
                )
            }
        }

        "should return 404 when latest schema of outgoing-dialog-message does not exist" {
            withSchemaRoutes(repository = FakeSchemaDocumentRepository(emptyList())) {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/outgoing-dialog-message/latest")

                response.status shouldBe HttpStatusCode.NotFound
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "No schemas found for schema type: outgoing-dialog-message"
                )
            }
        }

        "should get schema versions for incoming-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/incoming-dialog-message")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe "[1]"
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return latest schema for incoming-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/incoming-dialog-message/latest")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"incoming-dialog-message-v1"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return schema by version for incoming-dialog-message" {
            withSchemaRoutes {
                val response = client.get("/api/v1/schemas/incoming-dialog-message/v1")

                response.status shouldBe HttpStatusCode.OK
                response.body<String>() shouldBe """{"title":"incoming-dialog-message-v1"}"""
                response.contentType()?.withoutParameters() shouldBe ContentType.Application.Json
            }
        }

        "should return 404 for unknown version of incoming-dialog-message" {
            withSchemaRoutes {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/incoming-dialog-message/v999")

                response.status shouldBe HttpStatusCode.NotFound
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "Schema not found: incoming-dialog-message v999"
                )
            }
        }

        "should return 400 for invalid version of incoming-dialog-message" {
            withSchemaRoutes {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/incoming-dialog-message/vabc")

                response.status shouldBe HttpStatusCode.BadRequest
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "Invalid path parameter 'version': 'abc'. Expected integer."
                )
            }
        }

        "should return 404 when latest schema of incoming-dialog-message does not exist" {
            withSchemaRoutes(repository = FakeSchemaDocumentRepository(emptyList())) {
                client = createJsonEnabledClient()
                val response = client.get("/api/v1/schemas/incoming-dialog-message/latest")

                response.status shouldBe HttpStatusCode.NotFound
                response.body<ErrorResponse>() shouldBe ErrorResponse(
                    "No schemas found for schema type: incoming-dialog-message"
                )
            }
        }
    }
)

private fun ApplicationTestBuilder.createJsonEnabledClient(): HttpClient =
    createClient {
        install(ClientContentNegotiation) {
            json()
        }
    }

private fun withSchemaRoutes(
    repository: FakeSchemaDocumentRepository = FakeSchemaDocumentRepository(
        listOf(
            outgoingSchemaDocument(1),
            outgoingSchemaDocument(2),
            incomingSchemaDocument(1)
        )
    ),
    testBuilder: suspend ApplicationTestBuilder.() -> Unit
) = testApplication {
    install(ContentNegotiation) {
        json()
    }

    application {
        routing {
            externalRoutes(JsonSchemaService(repository))
        }
    }

    testBuilder()
}

private fun outgoingSchemaDocument(version: Int) = SchemaDocument(
    SchemaType.OUTGOING_DIALOG_MESSAGE,
    version,
    """{"title":"outgoing-dialog-message-v$version"}"""
)

private fun incomingSchemaDocument(version: Int) = SchemaDocument(
    SchemaType.INCOMING_DIALOG_MESSAGE,
    version,
    """{"title":"incoming-dialog-message-v$version"}"""
)
