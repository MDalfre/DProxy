package com.dproxy.ui


import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val normal by cssclass()
        val fieldStyle by cssclass()
        val buttonStyle by cssclass()
        val bottomText by cssclass()
        val checkBoxStyle by cssclass()
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

        textField and fieldStyle {
            startMargin = 12.px
            backgroundColor += c("#686c73")

        }

        button and buttonStyle {
            maxWidth = 500.px
            backgroundColor += c("#686c73")
            borderColor += box(
                top = Paint.valueOf("#000000"),
                right = Paint.valueOf("#000000"),
                left = Paint.valueOf("#000000"),
                bottom = Paint.valueOf("#000000")
            )
            borderRadius += box(5.px,5.px,5.px,5.px)
        }

        label and bottomText {
            fontWeight = FontWeight.EXTRA_BOLD
            fontSize = 15.px
        }

        checkBox and checkBoxStyle {
            fontSize = 13.px
        }
    }
}