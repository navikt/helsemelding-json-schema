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
                description = "Available schema types"
            }
        }
    }

    /* =============================================================
     * GET /schemas/{schemaType}
     * ============================================================= */

    const val GET_SCHEMA_VERSIONS = "/schemas/{schemaType}"

    val getSchemaVersionsDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "List schema versions"

        request {
            pathParameter<String>("schemaType") {
                description = "Schema type"
                required = true
            }
        }

        response {
            code(HttpStatusCode.OK) {
                description = "Available versions for the schema type"
            }
        }
    }

    /* =============================================================
     * GET /schemas/{schemaType}/latest
     * ============================================================= */

    const val GET_LATEST_SCHEMA = "/schemas/{schemaType}/latest"

    val getLatestSchemaDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "Get latest schema"

        request {
            pathParameter<String>("schemaType") {
                description = "Schema type"
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
     * GET /schemas/{schemaType}/v{version}
     * ============================================================= */

    const val GET_SCHEMA_VERSION = "/schemas/{schemaType}/v{version}"

    val getSchemaVersionDocs: RouteConfig.() -> Unit = {
        tags = listOf("schemas")
        summary = "Get schema by version"

        request {
            pathParameter<String>("schemaType") {
                description = "Schema type"
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
