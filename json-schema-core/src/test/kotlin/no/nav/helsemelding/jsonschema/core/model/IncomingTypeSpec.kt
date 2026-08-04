package no.nav.helsemelding.jsonschema.core.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IncomingTypeSpec : StringSpec(
    {
        "should use Norwegian labels as string values" {
            IncomingType.DIALOG_NOTE.toString() shouldBe "Dialognotat"
            IncomingType.DIALOG_RESPONSE.toString() shouldBe "Dialogsvar"
        }
    }
)
