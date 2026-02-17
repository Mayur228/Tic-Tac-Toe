package com.demo.tictactoe.ui.gamehost

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.ui.AiDifficulty

data class GameState(
    val board: List<String> = List(9) { "" },
    val myMark: String = "X",
    val isMyTurn: Boolean = false,
    val statusText: String = "Waiting…",
    val discoveredDevices: List<DeviceModel> = emptyList(),

    val gameOver: Boolean = false,
    val winner: String? = null,
    val awaitingAck: Boolean = false,

    val winningLine: List<Int>? = null,

    val connectionState: ConnectionState = ConnectionState.Idle,

    val isSinglePlayer: Boolean = false,
    val aiDifficulty: AiDifficulty = AiDifficulty.MEDIUM,

    val showFirstMoveDialog: Boolean = false,
    val isFirstMoveDecided: Boolean = false,

    val showDifficultyDialog: Boolean = false,

)

enum class ConnectionState {
    Idle, Advertising, Scanning, Connecting, Connected, Failed
}
