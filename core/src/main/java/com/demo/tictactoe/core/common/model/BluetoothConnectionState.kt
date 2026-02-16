package com.demo.tictactoe.core.common.model

sealed class BluetoothConnectionState {
    object Idle : BluetoothConnectionState()
    object Scanning : BluetoothConnectionState()
    object Connecting : BluetoothConnectionState()
    data class Connected(val peerName: String?) : BluetoothConnectionState()
    data class Error(val message: String) : BluetoothConnectionState()

    object Disconnected : BluetoothConnectionState()
}

