package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
annotation class SchemaVersion(
    val value: Int
)

@Description("A dialog message")
@Schema
@SchemaVersion(1)
data class DialogMessage(
    @Description("The current schema version") val version: Int,
    @Description("The id of the dialog message") val dialogId: String,
    @Description("When the dialog message was created")val createdAt: String,
    @Description("The payload of the dialog message") val payload: DialogPayload
)

data class DialogPayload(
    val messageText: String,
    val patientId: String
)
