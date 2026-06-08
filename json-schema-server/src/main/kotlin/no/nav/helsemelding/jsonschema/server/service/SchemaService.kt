package no.nav.helsemelding.jsonschema.server.service

import arrow.core.raise.Raise
import arrow.core.raise.ensureNotNull
import arrow.core.raise.withError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.SchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.SchemaServerError
import no.nav.helsemelding.jsonschema.server.model.SchemaMetadata

interface SchemaService {
    fun getSchemas(): List<SchemaMetadata>
    fun getVersions(schemaType: SchemaType): List<Int>
    fun Raise<SchemaServerError>.get(schemaType: SchemaType, version: Int): SchemaDocument
    fun Raise<SchemaServerError>.getLatest(schemaType: SchemaType): SchemaDocument
}

class JsonSchemaService(
    private val documentRepository: SchemaDocumentRepository
) : SchemaService {

    override fun getSchemas(): List<SchemaMetadata> =
        documentRepository.getAll()
            .map { SchemaMetadata(it.schemaType, it.version) }
            .sortedWith(schemaMetadataComparator)

    override fun getVersions(schemaType: SchemaType): List<Int> =
        documentRepository.getAll()
            .asSequence()
            .filter { it.schemaType == schemaType }
            .map { it.version }
            .sorted()
            .toList()

    override fun Raise<SchemaServerError>.get(
        schemaType: SchemaType,
        version: Int
    ): SchemaDocument =
        withError({ SchemaServerError.Schema(it) }) {
            documentRepository.get(schemaType, version).bind()
        }

    override fun Raise<SchemaServerError>.getLatest(
        schemaType: SchemaType
    ): SchemaDocument =
        documentRepository.getAll()
            .asSequence()
            .filter { it.schemaType == schemaType }
            .maxByOrNull { it.version }
            .let { document ->
                ensureNotNull(document) {
                    SchemaServerError.NoSchemasFound(schemaType)
                }
            }

    private companion object {
        val schemaMetadataComparator =
            compareBy<SchemaMetadata>({ it.schemaType.toString() }, { it.version })
    }
}
