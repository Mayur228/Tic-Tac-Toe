package com.demo.tictactoe.ui.gamehost

import com.demo.bluetooth_sdk.sdk.ConnectionState

data class GameState(
    val board: List<String> = List(9) { "" },
    val myMark: String = "X",
    val isMyTurn: Boolean = false,
    val statusText: String = "Waiting…",
    val discoveredDevices: List<String> = emptyList(),

    val gameOver: Boolean = false,
    val winner: String? = null,
    val awaitingAck: Boolean = false,

    val winningLine: List<Int>? = null,

    val connectionState: ConnectionState = ConnectionState.Default
)
