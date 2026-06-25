package no.nav.helsemelding.jsonschema.core.model

enum class OutgoingDialogMessageType(
    val process: String,
    val type: String,
    val kodeverk: Int,
    val kode: Int,
    val anvendelse: String
) {
    INNKALLING_DIALOGMOTE_DIALOG_FORESPORSEL_INNKALLING_DIALOGMOTE_2(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        1,
        "Innkalling dialogmøte 2"
    ),

    INNKALLING_DIALOGMOTE_DIALOG_FORESPORSEL_ENDRING_DIALOGMOTE_2(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        2,
        "Endring dialogmøte 2"
    ),

    INNKALLING_DIALOGMOTE_DIALOG_FORESPORSEL_INNKALLING_DIALOGMOTE_3(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        3,
        "Innkalling dialogmøte 3"
    ),

    INNKALLING_DIALOGMOTE_DIALOG_FORESPORSEL_ENDRING_DIALOGMOTE_3(
        "Innkalling dialogmøte",
        "DIALOG_FORESPØRSEL",
        8125,
        4,
        "Endring dialogmøte 3"
    ),

    FORESPOERSEL_OM_PASIENT_DIALOG_FORESPORSEL_FORESPORSEL_OM_PASIENT(
        "Forespørsel om pasient",
        "DIALOG_FORESPØRSEL",
        8129,
        1,
        "Forespørsel om pasient"
    ),

    FORESPOERSEL_OM_PASIENT_DIALOG_FORESPORSEL_PAMINNELSE_FORESPORSEL_OM_PASIENT(
        "Forespørsel om pasient",
        "DIALOG_FORESPØRSEL",
        8129,
        2,
        "Påminnelse forespørsel om pasient"
    ),

    OPPFOLGINGSPLAN_DIALOG_NOTAT_OPPFOLGINGSPLAN(
        "Oppfølgingsplan",
        "DIALOG_NOTAT",
        8127,
        1,
        "Oppfølgingsplan"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_FRISKMELDING_TIL_ARBEIDSFORMIDLING(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        2,
        "Friskmelding til arbeidsformidling"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_RETUR_AV_LEGEERKLARING(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        3,
        "Retur av legeerklæring"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_AVLYSNING_DIALOGMOTE(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        4,
        "Avlysning dialogmøte"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_UNNTAK_DIALOGMOTE(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        5,
        "Unntak dialogmøte"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_TILBAKEMELDING_FRA_NAV(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        6,
        "Tilbakemelding fra NAV"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_MELDING_FRA_NAV(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        8,
        "Melding fra NAV"
    ),

    HENVENDELSE_NAV_TIL_LEGE_DIALOG_NOTAT_INFORMASJON_FRA_NAV(
        "Henvendelse fra NAV til lege",
        "DIALOG_NOTAT",
        8127,
        9,
        "Informasjon fra NAV"
    );
}
