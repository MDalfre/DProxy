package com.example

import tornadofx.Controller
import tornadofx.getProperty
import tornadofx.property

class TextService: Controller() {

    var text: String by property("...")
    fun textProperty() = getProperty(TextService::text)

}
