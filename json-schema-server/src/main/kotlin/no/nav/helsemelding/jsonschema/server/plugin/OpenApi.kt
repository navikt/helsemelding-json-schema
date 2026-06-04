package no.nav.helsemelding.jsonschema.server.plugin

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.ExampleEncoder
import io.github.smiley4.ktoropenapi.config.OutputFormat.JSON
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.serialization.json.Json

fun Application.configureOpenApi() {
    install(OpenApi) {
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
            explicitNulls = false
        }
        examples {
            encoder(ExampleEncoder.kotlinx(json))
        }
        info {
            title = "JSON Schema API"
            version = "1.0.0"
            description = "API providing current schemas available"
        }
        pathFilter = { _, url -> url.firstOrNull() == "api" }
        outputFormat = JSON
    }
}
