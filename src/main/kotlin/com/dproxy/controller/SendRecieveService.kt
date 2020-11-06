package com.dproxy.controller

import com.dproxy.commons.ByteToHex
import com.dproxy.model.enum.Indicator
import com.dproxy.services.PacketTextService
import com.dproxy.services.SystemTextService
import com.dproxy.services.TestTextService
import tornadofx.Controller
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class SendRecieveService: Controller() {

    val packetTextService: PacketTextService by inject()
    val textService: SystemTextService by inject()

    private var byteToHex = ByteToHex()
    var packetNumber: Long = 0

    fun receive(connectServer: Socket, indicator: Indicator, log: Boolean): String {

        while (connectServer.isConnected) {

            val serverIn: InputStream = connectServer.getInputStream()
            if (serverIn.available() == 0) {
                break
            }
            val read = ByteArray(8024)
            val length: Int = serverIn.read(read)
            val byteArrayOutputStream = ByteArrayOutputStream()
            byteArrayOutputStream.write(read, 0, length)

            val readBytes = byteArrayOutputStream.toByteArray()
            val hexString = byteToHex.toHex(readBytes)
            packetNumber++
            when (log) {
                //true -> fire(PacketTextService("[$indicator] [$packetNumber] -> $hexString"))
                true -> fire(TestTextService(packetNumber,indicator.name,hexString.toString()))
            }
            return hexString.toString()
        }

        return "empty"

    }

    fun send(
        connectServer: Socket,
        packetToSend: String,
        indicator: Indicator,
        log: Boolean, iPacketNumber: Long = 0
    ) {

        if (packetToSend != "empty") {
            //Regex para remover espaços; Remover espaços depois no StringBuilder do serverReceive
            var stringPacket = packetToSend.replace("\\s".toRegex(), "")

            val byteArray = ByteArray(stringPacket.length / 2)
            for (i in byteArray.indices) {
                val index = i * 2
                val j = stringPacket.substring(index, index + 2).toInt(16)
                byteArray[i] = j.toByte()
            }
            when(log){
                true -> fire(TestTextService(iPacketNumber,indicator.name,packetToSend))
            }
            val serverOut: OutputStream = connectServer.getOutputStream()
            serverOut.write(byteArray)
        }

    }
}