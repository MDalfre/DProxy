package com.dproxy.ui.view

import com.dproxy.controller.ProxyService
import com.dproxy.model.PacketLog
import com.dproxy.services.StatusTextService
import com.dproxy.services.SystemTextService
import com.dproxy.services.TestTextService
import com.dproxy.ui.Styles
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.SelectionMode
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View("DProxy") {

    private val model = ViewModel()

    private val proxyService: ProxyService by inject()

    private val localPort = model.bind { SimpleIntegerProperty() }
    private val remotePort = model.bind { SimpleIntegerProperty() }
    private val remoteAddress = model.bind { SimpleStringProperty() }
    private var packet2Server = model.bind { SimpleStringProperty() }
    private var packet2Client = model.bind { SimpleStringProperty() }
    private var packetNumber = model.bind { SimpleLongProperty() }
    private var buttonStart = model.bind { SimpleStringProperty("Start") }
    private var runninStatus = model.bind { SimpleBooleanProperty(false) }
    var packetLog: ObservableList<PacketLog> = observableListOf()


    override val root = borderpane {

        prefWidth = 800.0
        prefHeight = 600.0


        subscribe<TestTextService> { event ->
            packetLog.add(PacketLog(event.number, event.from, event.packet))
        }

        subscribe<StatusTextService> { event ->
            runninStatus.set(event.status)
        }


        //TOP Section
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
        //LEFT Section
        left = vbox {
            paddingAll = 5.0
            prefWidth = 200.0
            prefHeight = 200.0
            style {
                backgroundColor += c("#474a4f")
            }

            label("Local port:") {
                addClass(Styles.normal)
            }

            textfield(localPort) {
                addClass(Styles.fieldStyle)
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
                addClass(Styles.fieldStyle)
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
                addClass(Styles.fieldStyle)
                prefWidth = 50.0
                text = "5051"
                hboxConstraints {
                    marginTop = 5.0
                }
                filterInput { it.controlNewText.isInt() }
            }
            label {

            }
            hbox {
                button(buttonStart) {
                    addClass(Styles.buttonStyle)
                    disableWhen(runninStatus)
                    style {
                        prefWidth = 100.px
                    }
                    hboxConstraints {
                        marginTop = 5.0
                        marginRight = 5.0
                    }

                    action {
                        buttonStart.set("Running")
                        runAsyncWithProgress {
                            proxyService.running = true
                            proxyService.setConnections(
                                localPort = localPort.value,
                                remoteAddress = remoteAddress.value,
                                remotePort = remotePort.value
                            )
                        }
                    }
                }
                button("Stop") {
                    addClass(Styles.buttonStyle)
                    disableWhen(!runninStatus)
                    style {
                        prefWidth = 100.px
                    }
                    hboxConstraints {
                        marginTop = 5.0
                    }
                    action {
                        buttonStart.set("Start")
                        proxyService.running = false
                    }
                }
            }


        }

        //CENTER Section
        center = vbox {
            style {
                backgroundColor += c("#474a4f")
            }
            paddingAll = 5.0
            label("System logs:") {
                addClass(Styles.normal)
            }
            listview<String> {
                prefHeight = 130.0
                items.add("[System] Welcome")
                subscribe<SystemTextService> { event ->
                    items.add(event.text)
                }
                selectionModel.selectionMode = SelectionMode.MULTIPLE
            }
            label("Packets:") {
                addClass(Styles.normal)
            }
            tableview(packetLog) {
                column("Number", PacketLog::number)
                column("From", PacketLog::from).cellFormat {
                    text = it
                    style {
                        when (it) {
                            "Server" -> {
                                textFill = Color.DARKBLUE
                                backgroundColor += Color.TRANSPARENT
                            }

                            "Client" -> {
                                textFill = Color.DARKGREEN
                                backgroundColor += Color.TRANSPARENT
                            }
                            "iServer" -> {
                                textFill = Color.WHITE
                                backgroundColor += Color.DARKBLUE
                            }
                            else -> {
                                textFill = Color.WHITE
                                backgroundColor += Color.DARKGREEN
                            }
                        }
                    }
                }
                column("Packet", PacketLog::packet)
                smartResize()
                selectionModel.selectionMode = SelectionMode.SINGLE
                onSelectionChange {
                    if (it?.from == "Server") {
                        packet2Client.set(it.packet)
                        packetNumber.set(it.number)
                    } else if (it?.from == "Client")  {
                        packet2Server.set(it.packet)
                        packetNumber.set(it.number)
                    }
                }


            }

        }

        //RIGHT Section
        right = vbox {
            style {
                backgroundColor += c("#474a4f")
            }
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
                disableWhen(!runninStatus)
                vboxConstraints {
                    marginTop = 10.0
                }
                action {
                    runAsyncWithProgress {
                        proxyService.sendPacket2Server(packet2Server.value, packetNumber.value)
                    }
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
                disableWhen(!runninStatus)
                vboxConstraints {
                    marginTop = 10.0
                }
                action {
                    runAsyncWithProgress {
                        proxyService.sendPacket2Client(packet2Client.value, packetNumber.value)
                    }
                }
            }

            label("Show packets:") {
                addClass(Styles.normal)
            }
            checkbox("Server") {
                addClass(Styles.checkBoxStyle)
                    .isSelected = true
                action { proxyService.serverLog = (isSelected) }
            }
            checkbox("Client") {
                addClass(Styles.checkBoxStyle)
                .isSelected = true
                action { proxyService.clientLog = (isSelected) }
            }

        }

        //BOTTOM Section
        bottom = hbox {
            subscribe<StatusTextService> { event ->
                if (event.status) {
                    style {
                        alignment = Pos.CENTER
                        backgroundColor += c("#34eb6b")
                    }
                } else {
                    style {
                        alignment = Pos.CENTER
                        backgroundColor += c("#c70c0c")
                    }
                }
            }
            style {
                alignment = Pos.CENTER
                backgroundColor += c("#c70c0c")
            }
            label("Disconnected") {
                addClass(Styles.bottomText)

                subscribe<StatusTextService> { event ->
                    text = if(event.status){
                        "Connected"
                    }else{
                        "Disconnected"
                    }
                }
            }
        }
    }
}
