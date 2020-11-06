package com.dproxy.model

import tornadofx.getProperty
import tornadofx.property

class PacketLog(
    number: Long,
    from: String,
    packet: String,
){
    var number by property(number)
    fun numberProperty() = getProperty(PacketLog::number)

    var from by property(from)
    fun fromProperty() = getProperty(PacketLog::from)

    var packet by property(packet)
    fun packetProperty() = getProperty(PacketLog::packet)
}
