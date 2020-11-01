package com.example

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.net.ServerSocket
import java.net.Socket


class SocketConfig: Controller() {

    val textService: TextService by inject()

    fun openConnectServer(ip: String, cnPort: Int): Socket {
        println("[System] Connecting to remote server at $ip ...")
        return Socket(ip, cnPort).also {fire(TextService("[System] Connected to $ip at port $cnPort")) }
    }

    fun openClient2Server(clPort: Int): Socket{
        fire(TextService("[System] Waiting for connections at port ${clPort}"))
        return  ServerSocket(clPort).accept().also { fire(TextService("[System] Client connected ! -> ${it.inetAddress}")) }
    }
}



