package com.example.ui.view

import com.example.controller.ProxyService
import com.example.controller.SocketConfig
import com.example.services.PacketTextService
import com.example.services.StatusTextService
import com.example.services.SystemTextService
import com.example.ui.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import tornadofx.*

class MainView : View("DProxy") {

    private val model = ViewModel()

    private val socketConfig: SocketConfig by inject()
    private val proxyService: ProxyService by inject()

    private val localPort = model.bind { SimpleIntegerProperty() }
    private val remotePort = model.bind { SimpleIntegerProperty() }
    private val remoteAddress = model.bind { SimpleStringProperty() }
    private val packet2Server = model.bind { SimpleStringProperty() }
    private var packet2Client = model.bind { SimpleStringProperty() }
    var packetTable: ListView<String> by singleAssign()


    override val root = borderpane() {

        prefWidth = 800.0
        prefHeight = 600.0

        top = hbox {
            style {
                backgroundColor += c("#bd5a2f")
            }
            paddingAll = 10.0
            //alignment = Pos.CENTER
            label("[DProxy] Tunnel") {
                addClass(Styles.heading)
            }
        }

        left = vbox {
            paddingAll = 5.0
            prefWidth = 200.0
            prefHeight = 200.0
//            style{
//                backgroundColor += c("#cecece")
//                borderColor += box(c("#a1a1a1"))
//            }

            label("Local port:") {
                addClass(Styles.normal)
            }

            textfield(localPort) {
                prefWidth = 50.0
                text = "5050"
                hboxConstraints {
                    marginTop = 5.0
                }
                filterInput { it.controlNewText.isInt() }
            }
            label("Remote Address:") {
                addClass(Styles.normal)
            }
            textfield(remoteAddress) {
                prefWidth = 100.0
                text = "127.0.0.1"
                hboxConstraints {
                    marginTop = 5.0
                }
            }

            label("Remote Port:") {
                addClass(Styles.normal)
            }
            textfield(remotePort) {
                prefWidth = 50.0
                text = "5051"
                hboxConstraints {
                    marginTop = 5.0
                }
                filterInput { it.controlNewText.isInt() }
            }
            label {

            }
            button {
                addClass(Styles.buttonStyle)
                this.text = "Start"
                hboxConstraints {
                    marginTop = 5.0
                }

                action {
                    runAsyncWithProgress {
                        proxyService.startProxy(
                            localPort = localPort.value,
                            remoteAddress = remoteAddress.value,
                            remotePort = remotePort.value
                        )
                    }
                }
            }


        }

        center = vbox {
            paddingAll = 5.0
            label("System logs:") {
                addClass(Styles.normal)
            }
            listview<String> {
                prefHeight = 130.0
                items.add("=== DProxy ===")
                subscribe<SystemTextService> { event ->
                    items.add(event.text)
                }
                selectionModel.selectionMode = SelectionMode.MULTIPLE
            }
            label("Packets:") {
                addClass(Styles.normal)
            }
            listview<String> {
                packetTable = this
                items.add("")
                subscribe<PacketTextService> { event ->
                    items.add(event.packet)
                }
                selectionModel.selectionMode = SelectionMode.MULTIPLE
            }

        }

        right = vbox {
            prefWidth = 200.00
            prefHeight = 200.0
            paddingAll = 5.0
            label("Send to server:") {
                addClass(Styles.normal)
            }
            textfield(packet2Server) {
                addClass(Styles.fieldStyle)
            }
            button("Send") {
                addClass(Styles.buttonStyle)
                vboxConstraints {
                    marginTop = 10.0
                }
                action {

                }
            }
            label("Send to client:") {
                addClass(Styles.normal)
            }
            textfield(packet2Client) {
                addClass(Styles.fieldStyle)
                prefWidth = 50.0
            }
            button("Send") {
                addClass(Styles.buttonStyle)
                vboxConstraints {
                    marginTop = 10.0
                }
                action {
                    runAsyncWithProgress {
                        socketConfig.openLocalConnection(localPort.value)
                    }
                }
            }

            label("Show packets:") {
                addClass(Styles.normal)
            }
            checkbox("Server") {
                check(true)
                action { proxyService.serverLog = (isSelected) }
            }
            checkbox("Client") {
                action { proxyService.clientLog = (isSelected) }
            }

        }

        bottom = hbox {
            style {
                backgroundColor += c("#34eb6b")
                alignment = Pos.CENTER
            }
            label("Disconnected") {
                addClass(Styles.bottomText)

                subscribe<StatusTextService> { event ->
                    text = event.status
                }
            }
        }
    }
}
