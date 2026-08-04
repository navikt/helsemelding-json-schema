package no.nav.helsemelding.jsonschema.core.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OutgoingTypeSpec : StringSpec(
    {
        "should use Norwegian labels as string values" {
            OutgoingType.DIALOG_REQUEST.toString() shouldBe "Dialogforespørsel"
            OutgoingType.DIALOG_NOTE.toString() shouldBe "Dialognotat"
        }
    }
)
