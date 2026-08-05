package no.nav.helsemelding.jsonschema.core.model

enum class OutgoingType(
    private val label: String
) {
    DIALOG_REQUEST("Dialogforespørsel"),
    DIALOG_NOTE("Dialognotat");

    override fun toString(): String = label
}
