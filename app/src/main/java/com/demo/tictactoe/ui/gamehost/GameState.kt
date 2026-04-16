package com.demo.tictactoe.ui.gamehost

sealed class HostState {
    data object Loading: HostState()
    data class Success(
        val statusText: String = "Waiting…",
        //val awaitingAck: Boolean = false,
        val connectionState: ConnectionState = ConnectionState.Idle,
    ): HostState()
    data class Error(val message: String): HostState()
}

enum class ConnectionState {
    Idle, Advertising, Scanning, Connecting, Connected, Failed, Cancelled
}
