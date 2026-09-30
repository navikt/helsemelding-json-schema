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

    @Description("Information about the healthcare provider specified in the message")
    val provider: Provider,

    @Description("Information about the signature")
    val signature: Signature,

    @Description("Document ID of the message in the document archive")
    val documentId: String
)

@Description("Information about the healthcare provider specified in the message")
@Schema
@Serializable
data class Provider(
    @Description("National identity number (11 digits) of the healthcare provider specified in the message")
    val ident: String,

    @Description("HPR-number in the Helsepersonellregisteret of the healthcare provider specified in the message")
    val hprNumber: String?,

    @Description("Information about the healthcare provider's office specified in the message")
    val office: ProviderOffice
)

@Description("Information about the healthcare provider's office specified in the message")
@Schema
@Serializable
data class ProviderOffice(
    @Description("Organisation number in the Enhetsregisteret of the healthcare provider's office")
    val orgNumber: String?,

    @Description("Name of the healthcare provider's office")
    val orgName: String,

    @Description("HER-id in the Adresseregisteret of the healthcare provider's office")
    val herId: String?
)

@Description("Information about the signature")
@Schema
@Serializable
data class Signature(
    @Description("National identity number (11 digits) of the healthcare provider who signed the message")
    val signingProviderIdent: String,

    @Description("Date and time the message was signed")
    val signedAt: String
)
