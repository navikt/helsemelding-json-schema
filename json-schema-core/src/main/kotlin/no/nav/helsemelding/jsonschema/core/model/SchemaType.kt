package no.nav.helsemelding.jsonschema.core.model

enum class SchemaType {
    DIALOG_MESSAGE;

    override fun toString(): String = name.lowercase().replace('_', '-')

    companion object {
        fun from(value: String): SchemaType? =
            entries
                .firstOrNull { it.toString() == value }
    }
}
