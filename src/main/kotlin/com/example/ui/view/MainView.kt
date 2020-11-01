package com.example.ui.view

import com.example.SocketConfig
import com.example.TextService
import com.example.ui.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class MainView : View("DProxy") {

    val model = ViewModel()
    val port = model.bind { SimpleIntegerProperty() }
    val socketConfig: SocketConfig by inject()
    val textService: TextService by inject()


    override val root = vbox {
        prefWidth = 400.0
        prefHeight = 600.0
        label(title) {
            addClass(Styles.heading)
        }
        textfield(port) {
            this.label {
                text = "Porta"
            }
        }
        button {
            this.text = "Butao"
            action {
                runAsyncWithProgress {
                    socketConfig.openClient2Server(port.value)
                }
            }
        }
        label(textService.textProperty()) {
            addClass(Styles.heading)

        }
    }
}
