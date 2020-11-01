package com.example.ui.view

import com.example.SocketConfig
import com.example.TextService
import com.example.ui.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.SelectionMode
import tornadofx.*

class MainView : View("DProxy") {

    val model = ViewModel()
    val port = model.bind { SimpleIntegerProperty() }
    val socketConfig: SocketConfig by inject()
    val test = listOf("asdasd", "qouie[").asObservable()
    val consolePath = System.getProperty("os.name") + " ~ " + System.getProperty("user.name") + ": "

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
            this.text = "Iniciar Proxy"
            action {
                runAsyncWithProgress {
                    socketConfig.openClient2Server(port.value)
                }
            }
        }
        listview<String> {
            items.add("=== DProxy ===")
            subscribe<TextService> { event ->
                items.add(event.text)
            }
            selectionModel.selectionMode = SelectionMode.MULTIPLE
        }


    }
}
