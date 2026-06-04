package no.nav.helsemelding.jsonschema.core.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.helsemelding.jsonschema.core.error.SchemaError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import java.io.File
import java.net.JarURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

private val log = KotlinLogging.logger {}

interface SchemaDocumentRepository {
    fun list(): List<SchemaDocument>
    fun get(messageType: SchemaType, version: Int): Either<SchemaError, SchemaDocument>
    fun latest(messageType: SchemaType): Either<SchemaError, SchemaDocument>
}

class JsonSchemaDocumentRepository : SchemaDocumentRepository {
    private val documents: List<SchemaDocument> by lazy {
        schemaResourcePaths()
            .map(::toSchemaDocument)
            .sortedWith(compareBy({ it.messageType }, { it.version }))
    }

    private val schemas: Map<Pair<SchemaType, Int>, SchemaDocument> by lazy {
        documents.associateBy { it.messageType to it.version }
    }

    override fun list(): List<SchemaDocument> = documents

    override fun get(
        messageType: SchemaType,
        version: Int
    ): Either<SchemaError, SchemaDocument> =
        schemas[messageType to version]?.right()
            ?: SchemaError.NotFound(
                messageType = messageType,
                version = version
            )
                .left()

    override fun latest(
        messageType: SchemaType
    ): Either<SchemaError, SchemaDocument> =
        documents
            .asSequence()
            .filter { it.messageType == messageType }
            .maxByOrNull { it.version }
            ?.right()
            ?: SchemaError.NoSchemasFound(messageType).left()

    private fun toSchemaDocument(path: String): SchemaDocument {
        val filename = path.substringAfterLast('/')

        val match = requireNotNull(schemaFileRegex.matchEntire(filename)) {
            "Invalid schema filename: $path"
        }

        val messageType = SchemaType.from(match.groupValues[1])
            ?: error("Unknown schema type: ${match.groupValues[1]}")

        val version = match.groupValues[2].toInt()

        return SchemaDocument(
            messageType = messageType,
            version = version,
            content = readResource(path)
        )
    }

    private fun schemaResourcePaths(): List<String> {
        val classLoader = javaClass.classLoader

        val resource = requireNotNull(classLoader.getResource(SCHEMA_ROOT)) {
            "Schema resource directory not found: $SCHEMA_ROOT"
        }

        return when (resource.protocol) {
            "file" -> loadFromFileSystem(resource)
                .also { log.info { "Loaded schema resources from directory: $SCHEMA_ROOT" } }

            "jar" -> loadFromJar(resource)
                .also { log.info { "Loaded schemas from JAR resource path: $SCHEMA_ROOT" } }

            else -> error("Unsupported schema resource protocol: ${resource.protocol}")
        }
    }

    private fun loadFromFileSystem(resource: URL): List<String> {
        val root = Paths.get(resource.toURI())

        return Files.walk(root).use { paths ->
            paths
                .filter(Files::isRegularFile)
                .map { root.relativize(it).toString().replace(File.separatorChar, '/') }
                .filter { it.endsWith(".schema.json") }
                .map { "$SCHEMA_ROOT/$it" }
                .toList()
        }
    }

    private fun loadFromJar(resource: URL): List<String> {
        val connection = resource.openConnection() as JarURLConnection

        return connection.jarFile
            .entries()
            .asSequence()
            .map { it.name }
            .filter { it.startsWith("$SCHEMA_ROOT/") }
            .filter { it.endsWith(".schema.json") }
            .toList()
    }

    private fun readResource(path: String): String =
        requireNotNull(javaClass.classLoader.getResourceAsStream(path)) {
            "Schema resource not found: $path"
        }
            .bufferedReader()
            .use { it.readText() }

    private companion object {
        const val SCHEMA_ROOT = "schemas"
        val schemaFileRegex = Regex("""([a-z0-9-]+)-v(\d+)\.schema\.json""")
    }
}

class FakeSchemaDocumentRepository(
    documents: List<SchemaDocument>
) : SchemaDocumentRepository {
    private val documents: List<SchemaDocument> =
        documents.sortedWith(compareBy({ it.messageType }, { it.version }))

    private val schemas: Map<Pair<SchemaType, Int>, SchemaDocument> =
        this.documents.associateBy { it.messageType to it.version }

    override fun list(): List<SchemaDocument> = documents

    override fun get(
        messageType: SchemaType,
        version: Int
    ): Either<SchemaError, SchemaDocument> =
        schemas[messageType to version]?.right()
            ?: SchemaError.NotFound(
                messageType = messageType,
                version = version
            )
                .left()

    override fun latest(
        messageType: SchemaType
    ): Either<SchemaError, SchemaDocument> =
        documents
            .asSequence()
            .filter { it.messageType == messageType }
            .maxByOrNull { it.version }
            ?.right()
            ?: SchemaError.NoSchemasFound(messageType).left()
}
