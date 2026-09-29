package no.nav.helsemelding.jsonschema.core.model

import kotlinx.schema.Description
import kotlinx.schema.Schema
import kotlinx.serialization.Serializable

@Description("An incoming dialog message")
@Schema
@SchemaVersion(1)
@Serializable
data class IncomingDialogMessage(
    @Description("The current schema version")
    val version: Int,

    @Description("Unique identifier of the dialog message")
    val id: String,

    @Description("Type of dialog message")
    val type: IncomingDialogMessageType,

    @Description("Date and time the dialog message was received (UTC)")
    val receivedAt: String,

    @Description("National identity number (11 digits) of the patient")
    val patientIdent: String,

    @Description("Conversation this message belongs to")
    val conversationReference: ConversationReference?,

    @Description("Message text")
    val message: String?,

    @Description("Number of attachments")
    val numberOfAttachments: Int,

    @Description("Information about the doctor specified in the message")
    val provider: Provider,

    @Description("Information about the signature")
    val signature: Signature,

    @Description("Document ID of the message in the document archive")
    val documentId: String
)

@Description("Information about the doctor specified in the message")
@Schema
@Serializable
data class Provider(
    @Description("National identity number (11 digits) of the doctor specified in the message")
    val ident: String,

    @Description("HPR-number of the doctor specified in the message in the Helsepersonellregisteret")
    val hprNumber: String?,

    @Description("Information about the provider office specified in the message")
    val office: ProviderOffice
)

@Description("Information about the doctor's office specified in the message")
@Schema
@Serializable
data class ProviderOffice(
    @Description("Organisation number of the doctor's office in the Enhetsregisteret")
    val orgNumber: String?,

    @Description("Name of the doctor's office")
    val orgName: String,

    @Description("HER-id of the doctor's office in the Adresseregisteret")
    val herId: String?
)

@Description("Information about the signature")
@Schema
@Serializable
data class Signature(
    @Description("National identity number (11 digits) of the doctor who signed the message")
    val signingProviderIdent: String,

    @Description("Date and time the message was signed")
    val signedAt: String
)
