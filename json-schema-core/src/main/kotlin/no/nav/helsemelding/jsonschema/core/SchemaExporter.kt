package no.nav.helsemelding.jsonschema.core

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import no.nav.helsemelding.jsonschema.core.model.IncomingDialogMessage
import no.nav.helsemelding.jsonschema.core.model.OutgoingDialogMessage
import no.nav.helsemelding.jsonschema.core.model.SchemaDefinition
import no.nav.helsemelding.jsonschema.core.model.SchemaVersion
import no.nav.helsemelding.jsonschema.core.model.jsonSchemaString
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.reflect.full.findAnnotation

private val log = KotlinLogging.logger {}
private val prettyJson = Json { prettyPrint = true }

fun main(args: Array<String>) {
    require(args.size == 1) { "Expected output directory as first argument" }

    val outputDir = Path.of(args.first())
    outputDir.createDirectories()

    val schemas = listOf(
        outgoingDialogMessageSchemaDefinition(),
        incomingDialogMessageSchemaDefinition()
    )

    schemas.forEach { schema ->
        val file = outputDir.resolve("${schema.fileName}-v${schema.version}.schema.json")

        if (file.exists()) {
            log.warn { "Schema already exists: $file. Bump version or delete intentionally." }
        } else {
            file.writeText(schema.schema)
        }
    }
}

private fun outgoingDialogMessageSchemaDefinition(): SchemaDefinition = SchemaDefinition(
    fileName = "outgoing-dialog-message",
    version = OutgoingDialogMessage::class.findAnnotation<SchemaVersion>()?.value
        ?: error("Missing @SchemaVersion on ${OutgoingDialogMessage::class.qualifiedName}"),
    schema = OutgoingDialogMessage::class.jsonSchemaString.withUuidDefinition()
)

private fun String.withUuidDefinition(): String {
    val schema = Json.parseToJsonElement(this).jsonObject
    val definitions = schema.getValue("\$defs").jsonObject
    val uuidDefinition = buildJsonObject {
        put("type", "string")
        put("format", "uuid")
        put(
            "pattern",
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        )
    }
    val normalizedSchema = JsonObject(
        schema + ("\$defs" to JsonObject(definitions + ("kotlin.uuid.Uuid" to uuidDefinition)))
    )

    return prettyJson.encodeToString(normalizedSchema)
}

private fun incomingDialogMessageSchemaDefinition(): SchemaDefinition = SchemaDefinition(
    fileName = "incoming-dialog-message",
    version = IncomingDialogMessage::class.findAnnotation<SchemaVersion>()?.value
        ?: error("Missing @SchemaVersion on ${IncomingDialogMessage::class.qualifiedName}"),
    schema = IncomingDialogMessage::class.jsonSchemaString
)
