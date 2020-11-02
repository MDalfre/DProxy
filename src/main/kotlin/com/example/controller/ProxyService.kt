package com.example.controller

import com.example.model.enum.Indicator
import com.example.services.SystemTextService
import tornadofx.Controller
import java.io.IOException

class ProxyService : Controller() {

    val textService: SystemTextService by inject()
    var serverLog = true
    var clientLog = true

    fun startProxy(localPort: Int, remotePort: Int, remoteAddress: String) {
        val sendReceive = SendRecieveService()
        if (localPort == 0 || remotePort == 0) {
            throw IOException("Ports must not be null")
        }
        val localConnection = SocketConfig().openLocalConnection(localPort)
        val remoteConnection = SocketConfig().openRemoteConnection(remoteAddress, remotePort)



        do {
            // --- Server Server ( recebe pacotes servidor )
            var serverPackets = sendReceive.receive(remoteConnection, Indicator.Server, serverLog)

            //serverPackets = filter(serverPackets)

            // --- Server Client
            sendReceive.send(localConnection, serverPackets, Indicator.Client, false)

            // --- Client Server
            val clientPackets = sendReceive.receive(localConnection, Indicator.Client, clientLog)

            // --- Server Server ( envia pacotes servidor )
            sendReceive.send(remoteConnection, clientPackets, Indicator.Server, false)

        } while (localConnection.isConnected || remoteConnection.isConnected)

        fire(SystemTextService("[System] Disconnected from the server"))


    }
}