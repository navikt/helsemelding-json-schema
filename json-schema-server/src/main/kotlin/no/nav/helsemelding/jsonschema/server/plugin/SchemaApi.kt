package no.nav.helsemelding.jsonschema.server.plugin

import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

object SchemaApi {

    /* =============================================================
     * GET /schemas
     * ============================================================= */

    const val GET_SCHEMAS = "/schemas"

    val getSchemasDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "List schemas"

        response {
            code(HttpStatusCode.OK) {
                description = "Available schema message types"
            }
        }
    }

    /* =============================================================
     * GET /schemas/{messageType}
     * ============================================================= */

    const val GET_SCHEMA_VERSIONS = "/schemas/{messageType}"

    val getSchemaVersionsDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "List schema versions"

        request {
            pathParameter<String>("messageType") {
                description = "Schema message type"
                required = true
            }
        }

        response {
            code(HttpStatusCode.OK) {
                description = "Available versions for the message type"
            }
        }
    }

    /* =============================================================
     * GET /schemas/{messageType}/latest
     * ============================================================= */

    const val GET_LATEST_SCHEMA = "/schemas/{messageType}/latest"

    val getLatestSchemaDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "Get latest schema"

        request {
            pathParameter<String>("messageType") {
                description = "Schema message type"
                required = true
            }
        }

        response {
            code(HttpStatusCode.OK) {
                description = "Latest JSON schema"
                body<String> {
                    mediaTypes(ContentType.Application.Json)
                }
            }
        }
    }

    /* =============================================================
     * GET /schemas/{messageType}/v{version}
     * ============================================================= */

    const val GET_SCHEMA_VERSION = "/schemas/{messageType}/v{version}"

    val getSchemaVersionDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "Get schema by version"

        request {
            pathParameter<String>("messageType") {
                description = "Schema message type"
                required = true
            }

            pathParameter<Int>("version") {
                description = "Schema version"
                required = true
            }
        }

        response {
            code(HttpStatusCode.OK) {
                description = "Versioned JSON schema"
                body<String> {
                    mediaTypes(ContentType.Application.Json)
                }
            }
        }
    }
}
