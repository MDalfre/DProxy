package com.example.controller

import com.example.services.StatusTextService
import com.example.services.SystemTextService
import tornadofx.Controller
import java.net.ServerSocket
import java.net.Socket


class SocketConfig : Controller() {

    val textService: SystemTextService by inject()

    fun openRemoteConnection(ip: String, cnPort: Int): Socket {
        fire(SystemTextService("[System] Connecting to remote server at $ip ..."))
        return Socket(ip, cnPort).also {
            fire(SystemTextService("[System] Connected to $ip at port $cnPort"))
            fire(StatusTextService("Connected !"))
        }
    }

    fun openLocalConnection(clPort: Int): Socket {
        fire(SystemTextService("[System] Waiting for connections at port ${clPort}"))
        return ServerSocket(clPort).accept()
            .also { fire(SystemTextService("[System] Client connected ! -> ${it.inetAddress}")) }
    }
}



