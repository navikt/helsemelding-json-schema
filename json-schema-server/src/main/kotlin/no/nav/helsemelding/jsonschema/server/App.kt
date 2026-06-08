package no.nav.helsemelding.jsonschema.server

import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.core.raise.result
import arrow.fx.coroutines.resourceScope
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.netty.Netty
import io.ktor.utils.io.CancellationException
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.coroutines.awaitCancellation
import no.nav.helsemelding.jsonschema.core.repository.JsonSchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.plugin.configureContentNegotiation
import no.nav.helsemelding.jsonschema.server.plugin.configureMetrics
import no.nav.helsemelding.jsonschema.server.plugin.configureOpenApi
import no.nav.helsemelding.jsonschema.server.plugin.configureRoutes
import no.nav.helsemelding.jsonschema.server.service.JsonSchemaService
import no.nav.helsemelding.jsonschema.server.service.SchemaService

private val log = KotlinLogging.logger {}

fun main() = SuspendApp {
    result {
        resourceScope {
            val deps = dependencies()

            val schemaRepository = JsonSchemaDocumentRepository()
            val schemaService = JsonSchemaService(schemaRepository)

            server(
                Netty,
                port = config().server.port.value,
                preWait = config().server.preWait,
                module = schemaModule(deps.meterRegistry, schemaService)
            )

            awaitCancellation()
        }
    }
        .onFailure { error -> if (error !is CancellationException) logError(error) }
}

internal fun schemaModule(
    meterRegistry: PrometheusMeterRegistry,
    schemaService: SchemaService
): Application.() -> Unit {
    return {
        configureMetrics(meterRegistry)
        configureContentNegotiation()
        configureRoutes(meterRegistry, schemaService)
        configureOpenApi()
    }
}

private fun logError(t: Throwable) = log.error(t) { "Shutdown processing service" }
