package no.nav.helsemelding.jsonschema.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class OutgoingDialogMessageType(
    val process: String,
    val messageType: String,
    val codeSystem: Int,
    val code: Int,
    val application: String
) {
    MEETING_INVITATION_2(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        1,
        "Innkalling dialogmøte 2"
    ),

    MEETING_RESCHEDULE_2(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        2,
        "Endring dialogmøte 2"
    ),

    MEETING_INVITATION_3(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        3,
        "Innkalling dialogmøte 3"
    ),

    MEETING_RESCHEDULE_3(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        4,
        "Endring dialogmøte 3"
    ),

    PATIENT_REQUEST(
        "Forespørsel om pasient",
        "DIALOG_FORESPØRSEL",
        8129,
        1,
        "Forespørsel om pasient"
    ),

    PATIENT_REQUEST_REMINDER(
        "Forespørsel om pasient",
        "DIALOG_FORESPØRSEL",
        8129,
        2,
        "Påminnelse forespørsel om pasient"
    ),

    FOLLOW_UP_PLAN(
        "Oppfølgingsplan",
        "DIALOG_NOTAT",
        8127,
        1,
        "Oppfølgingsplan"
    ),

    RETURN_TO_WORK_NOTIFICATION(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        2,
        "Friskmelding til arbeidsformidling"
    ),

    MEDICAL_CERTIFICATE_RETURN(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        3,
        "Retur av legeerklæring"
    ),

    MEETING_CANCELLATION(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        4,
        "Avlysning dialogmøte"
    ),

    MEETING_EXEMPTION(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        5,
        "Unntak dialogmøte"
    ),

    NAV_FEEDBACK(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        6,
        "Tilbakemelding fra NAV"
    ),

    NAV_MESSAGE(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        8,
        "Melding fra NAV"
    ),

    NAV_INFORMATION(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        9,
        "Informasjon fra NAV"
    );
}
