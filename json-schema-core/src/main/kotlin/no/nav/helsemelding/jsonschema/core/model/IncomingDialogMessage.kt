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
    val patientPersonalId: String,

    @Description("Conversation this message belongs to")
    val conversationReference: ConversationReference?,

    @Description("Message text")
    val message: String?,

    @Description("Number of attachments")
    val numberOfAttachments: Int,

    @Description("National identity number (11 digits) of the doctor specified in the message")
    val providerPersonalId: String,

    @Description("HPR-id of the doctor specified in the message")
    val providerHprId: String?,

    @Description("Organisation number of the doctor's office")
    val providerOfficeOrgNr: String?,

    @Description("HER-id of the doctor's office")
    val providerOfficeHerId: String?,

    @Description("Name of the doctor's office")
    val providerOfficeOrgName: String,

    @Description("National identity number (11 digits) of the doctor who signed the message")
    val signingProviderPersonalId: String,

    @Description("Date and time the message was signed")
    val signedAt: String,

    @Description("Document ID of the message in the document archive")
    val documentIdNote: String

)
