package no.nav.helsemelding.jsonschema.server.service

import arrow.core.raise.Raise
import arrow.core.raise.withError
import no.nav.helsemelding.jsonschema.core.model.SchemaDocument
import no.nav.helsemelding.jsonschema.core.model.SchemaMetadata
import no.nav.helsemelding.jsonschema.core.model.SchemaType
import no.nav.helsemelding.jsonschema.core.repository.SchemaDocumentRepository
import no.nav.helsemelding.jsonschema.server.SchemaServerError

interface SchemaService {
    fun listSchemas(): List<SchemaMetadata>
    fun listVersions(messageType: SchemaType): List<Int>
    fun Raise<SchemaServerError>.get(messageType: SchemaType, version: Int): SchemaDocument
    fun Raise<SchemaServerError>.latest(messageType: SchemaType): SchemaDocument
}

class JsonSchemaService(
    private val documentRepository: SchemaDocumentRepository
) : SchemaService {

    override fun listSchemas(): List<SchemaMetadata> =
        documentRepository.list()
            .map { SchemaMetadata(it.messageType, it.version) }
            .sortedWith(schemaMetadataComparator)

    override fun listVersions(messageType: SchemaType): List<Int> =
        documentRepository.list()
            .asSequence()
            .filter { it.messageType == messageType }
            .map { it.version }
            .sorted()
            .toList()

    override fun Raise<SchemaServerError>.get(
        messageType: SchemaType,
        version: Int
    ): SchemaDocument =
        withError({ SchemaServerError.Schema(it) }) {
            documentRepository.get(messageType, version).bind()
        }

    override fun Raise<SchemaServerError>.latest(
        messageType: SchemaType
    ): SchemaDocument =
        withError({ SchemaServerError.Schema(it) }) {
            documentRepository.latest(messageType).bind()
        }

    private companion object {
        val schemaMetadataComparator =
            compareBy<SchemaMetadata>({ it.messageType.toString() }, { it.version })
    }
}
