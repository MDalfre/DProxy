package com.example.ui


import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val normal by cssclass()
        val fieldStyle by cssclass()
        val buttonStyle by cssclass()
        val bottomText by cssclass()
    }

    init {
        label and heading {
            //padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            textFill = c("#ffffff")
        }

        label and normal {
            padding = box(vertical = 8.px, horizontal = 5.px)
            fontSize = 15.px
        }

        textArea and fieldStyle {
            padding = box(15.px)
            startMargin = 12.px
        }

        button and buttonStyle {
            maxWidth = 500.px
        }

        label and bottomText {
            fontWeight = FontWeight.EXTRA_BOLD
        }
    }
}