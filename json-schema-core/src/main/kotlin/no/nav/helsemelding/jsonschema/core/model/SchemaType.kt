package no.nav.helsemelding.jsonschema.core.model

enum class SchemaType {
    OUTGOING_DIALOG_MESSAGE,
    INCOMING_DIALOG_MESSAGE;

    override fun toString(): String = name.lowercase().replace('_', '-')

    companion object {
        fun from(value: String): SchemaType? =
            entries
                .firstOrNull { it.toString() == value }
    }
}
