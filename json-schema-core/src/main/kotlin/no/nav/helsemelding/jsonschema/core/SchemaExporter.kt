package no.nav.helsemelding.jsonschema.core

import no.nav.helsemelding.jsonschema.core.model.DialogMessage
import no.nav.helsemelding.jsonschema.core.model.SchemaDefinition
import no.nav.helsemelding.jsonschema.core.model.SchemaVersion
import no.nav.helsemelding.jsonschema.core.model.jsonSchemaString
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.reflect.full.findAnnotation

fun main(args: Array<String>) {
    require(args.size == 1) { "Expected output directory as first argument" }

    val outputDir = Path.of(args.first())
    outputDir.createDirectories()

    val schemas = listOf(dialogMessageSchemaDefinition())

    schemas.forEach { schema ->
        val file = outputDir.resolve("${schema.fileName}-v${schema.version}.schema.json")

        require(!file.exists()) {
            "Schema already exists: $file. Bump version or delete intentionally."
        }

        file.writeText(schema.schema)
    }
}

private fun dialogMessageSchemaDefinition(): SchemaDefinition = SchemaDefinition(
    fileName = "dialog-message",
    version = DialogMessage::class.findAnnotation<SchemaVersion>()?.value
        ?: error("Missing @SchemaVersion on ${DialogMessage::class.qualifiedName}"),
    schema = DialogMessage::class.jsonSchemaString
)
