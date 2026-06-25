package no.nav.helsemelding.jsonschema.core.model

enum class IncomingDialogMessageType(
    val process: String,
    val type: String,
    val kodeverk: Int,
    val kode: Int,
    val anvendelse: String
) {
    INNKALLING_DIALOGMOTE_DIALOG_SVAR_JA_JEG_KOMMER(
        "Innkalling dialogmøte",
        "DIALOG_SVAR",
        8126,
        1,
        "Ja, jeg kommer"
    ),

    INNKALLING_DIALOGMOTE_DIALOG_SVAR_JEG_ONSKER_NYTT_MOTETIDSPUNKT(
        "Innkalling dialogmøte",
        "DIALOG_SVAR",
        8126,
        2,
        "Jeg ønsker nytt møtetidspunkt"
    ),

    INNKALLING_DIALOGMOTE_DIALOG_SVAR_JEG_KAN_IKKE_KOMME_BEGRENNELSE_FOR_MANGLEDE_OPPMOTE(
        "Innkalling dialogmøte",
        "DIALOG_SVAR",
        8126,
        3,
        "Jeg kan ikke komme / begrunnelse for manglende oppmøte"
    ),

    FORESPOERSEL_OM_PASIENT_DIALOG_SVAR_SVAR_PA_FORESPORSEL(
        "Forespørsel om pasient",
        "DIALOG_SVAR",
        9069,
        5,
        "Svar på forespørsel"
    ),

    HENVENDELSE_LEGE_TIL_NAV_DIALOG_NOTAT_HENVENDELSE_OM_SYKEFRAVARSOPPFOLGING(
        "Henvendelse fra lege til NAV",
        "DIALOG_NOTAT",
        8128,
        1,
        "Henvendelse om sykefraværsoppfølging"
    ),

    HENVENDELSE_LEGE_TIL_NAV_DIALOG_NOTAT_HENVENDELSE_OM_PASIENT(
        "Henvendelse fra lege til NAV",
        "DIALOG_NOTAT",
        8128,
        2,
        "Henvendelse om pasient"
    );
}
