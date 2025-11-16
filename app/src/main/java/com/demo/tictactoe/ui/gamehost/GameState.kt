package com.demo.tictactoe.ui.gamehost

import android.bluetooth.BluetoothDevice

data class GameState(
    val board: List<String> = List(9) { "" },
    val myMark: String = "X",
    val isMyTurn: Boolean = false,
    val statusText: String = "Waiting…",
    val discoveredDevices: List<BluetoothDevice> = emptyList(),

    val gameOver: Boolean = false,
    val winner: String? = null,
    val awaitingAck: Boolean = false,

    val winningLine: List<Int>? = null,

    val connectionState: ConnectionState = ConnectionState.Idle
)

enum class ConnectionState {
    Idle, Advertising, Scanning, Connecting, Connected, Failed
}
