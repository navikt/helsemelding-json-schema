package no.nav.helsemelding.jsonschema.server.plugin

import arrow.core.raise.recover
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.server.ErrorResponse
import no.nav.helsemelding.jsonschema.server.RequestError
import no.nav.helsemelding.jsonschema.server.SchemaServerError
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.GET_LATEST_SCHEMA
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.GET_SCHEMAS
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.GET_SCHEMA_VERSION
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.GET_SCHEMA_VERSIONS
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.getLatestSchemaDocs
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.getSchemaVersionDocs
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.getSchemaVersionsDocs
import no.nav.helsemelding.jsonschema.server.plugin.SchemaApi.getSchemasDocs
import no.nav.helsemelding.jsonschema.server.schemaType
import no.nav.helsemelding.jsonschema.server.service.SchemaService
import no.nav.helsemelding.jsonschema.server.version

fun Application.configureRoutes(
    registry: PrometheusMeterRegistry,
    schemaService: SchemaService
) {
    routing {
        swaggerRoutes()
        internalRoutes(registry)
        externalRoutes(schemaService)
    }
}

fun Route.swaggerRoutes() {
    route("api.json") {
        openApi()
    }
    route("swagger") {
        swaggerUI("/api.json") {
        }
    }
}

fun Route.internalRoutes(registry: PrometheusMeterRegistry) {
    get("/prometheus") {
        call.respond(registry.scrape())
    }
    route("/internal") {
        get("/health/liveness") {
            call.respondText("I'm alive! :)")
        }
        get("/health/readiness") {
            call.respondText("I'm ready! :)")
        }
    }
}

fun Route.externalRoutes(schemaService: SchemaService) {
    route("/api/v1") {
        get(GET_SCHEMAS, getSchemasDocs) {
            call.respond(schemaService.listSchemas())
        }

        get(GET_SCHEMA_VERSIONS, getSchemaVersionsDocs) {
            recover({
                val schemaType = schemaType(call)
                call.respond(schemaService.listVersions(schemaType))
            }) { e: SchemaServerError -> call.respondSchemaServerError(e) }
        }

        get(GET_LATEST_SCHEMA, getLatestSchemaDocs) {
            recover({
                with(schemaService) {
                    val schemaType = schemaType(call)
                    val schema = latest(schemaType)

                    call.respondText(
                        text = schema.content,
                        contentType = ContentType.Application.Json
                    )
                }
            }) { e: SchemaServerError -> call.respondSchemaServerError(e) }
        }

        get(GET_SCHEMA_VERSION, getSchemaVersionDocs) {
            recover({
                with(schemaService) {
                    val schemaType = schemaType(call)
                    val version = version(call)
                    val schema = get(schemaType, version)

                    call.respondText(
                        text = schema.content,
                        contentType = ContentType.Application.Json
                    )
                }
            }) { e: SchemaServerError -> call.respondSchemaServerError(e) }
        }
    }
}

suspend fun ApplicationCall.respondSchemaServerError(error: SchemaServerError) {
    when (error) {
        is SchemaServerError.Request -> respondRequestError(error.error)
        is SchemaServerError.Schema -> respondSchemaError(error.error)
    }
}

private suspend fun ApplicationCall.respondRequestError(error: RequestError) {
    respond(
        status = HttpStatusCode.BadRequest,
        message = ErrorResponse(error.message)
    )
}

suspend fun ApplicationCall.respondSchemaError(error: SchemaError) {
    when (error) {
        is SchemaError.NotFound ->
            respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    "Schema not found: ${error.schemaType} v${error.version}"
                )
            )

        is SchemaError.NoSchemasFound ->
            respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    "No schemas found for message type: ${error.schemaType}"
                )
            )

        is SchemaError.InvalidSchema ->
            respond(
                HttpStatusCode.UnprocessableEntity,
                ErrorResponse(
                    "Invalid schema: ${error.reason}"
                )
            )
    }
}
