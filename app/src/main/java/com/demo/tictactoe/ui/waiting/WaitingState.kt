package com.demo.tictactoe.ui.waiting

import com.demo.tictactoe.ui.gamehost.ConnectionState

data class WaitingState(
    val connectionState: ConnectionState = ConnectionState.Idle,
    val message: String = "Preparing..."
)