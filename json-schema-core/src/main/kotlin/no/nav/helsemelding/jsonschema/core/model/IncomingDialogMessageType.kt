package no.nav.helsemelding.jsonschema.core.model

import kotlinx.serialization.Serializable
import no.nav.helsemelding.jsonschema.core.model.IncomingType.DIALOG_NOTE
import no.nav.helsemelding.jsonschema.core.model.IncomingType.DIALOG_RESPONSE

@Serializable
enum class IncomingDialogMessageType(
    val process: String,
    val messageType: IncomingType,
    val codeSystem: Int,
    val code: Int,
    val application: String
) {
    ACCEPTS_MEETING_INVITATION(
        "Innkalling dialogmøte",
        DIALOG_RESPONSE,
        8126,
        1,
        "Ja, jeg kommer"
    ),

    REQUESTS_NEW_MEETING_TIME(
        "Innkalling dialogmøte",
        DIALOG_RESPONSE,
        8126,
        2,
        "Jeg ønsker nytt møtetidspunkt"
    ),

    DECLINES_MEETING_WITH_REASON(
        "Innkalling dialogmøte",
        DIALOG_RESPONSE,
        8126,
        3,
        "Jeg kan ikke komme / begrunnelse for manglende oppmøte"
    ),

    PATIENT_REQUEST_RESPONSE(
        "Forespørsel om pasient",
        DIALOG_RESPONSE,
        9069,
        5,
        "Svar på forespørsel"
    ),

    SICK_LEAVE_FOLLOW_UP_INQUIRY(
        "Henvendelse fra lege til NAV",
        DIALOG_NOTE,
        8128,
        1,
        "Henvendelse om sykefraværsoppfølging"
    ),

    PATIENT_INQUIRY(
        "Henvendelse fra lege til NAV",
        DIALOG_NOTE,
        8128,
        2,
        "Henvendelse om pasient"
    );
}
