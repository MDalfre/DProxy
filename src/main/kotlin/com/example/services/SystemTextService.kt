package com.example.services

import tornadofx.FXEvent

class SystemTextService(val text: String): FXEvent()

class PacketTextService(val packet: String): FXEvent()

class StatusTextService(val status: String): FXEvent()
