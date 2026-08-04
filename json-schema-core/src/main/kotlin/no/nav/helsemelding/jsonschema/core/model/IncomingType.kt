package no.nav.helsemelding.jsonschema.core.model

enum class IncomingType(
    private val label: String
) {
    DIALOG_NOTE("Dialognotat"),
    DIALOG_RESPONSE("Dialogsvar");

    override fun toString(): String = label
}
