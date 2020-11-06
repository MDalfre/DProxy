package com.dproxy.services

import tornadofx.FXEvent

class SystemTextService(val text: String): FXEvent()

class PacketTextService(val packet: String): FXEvent()

class StatusTextService(val status: Boolean): FXEvent()

class TestTextService(val number:Long, val from: String, val packet: String): FXEvent()
