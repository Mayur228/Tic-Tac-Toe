package com.demo.tictactoe.ui.game

import com.demo.tictactoe.ui.AiDifficulty
import com.demo.tictactoe.ui.gamehost.ConnectionState

sealed class GameBoardState {
    data object Loading: GameBoardState()
    data class GameData(
        val board: List<String> = List(9) { "" },
        val myMark: String = "X",
        val isMyTurn: Boolean = false,
        val statusText: String = "Waiting…",

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
    ): GameBoardState()
    data class Error(val message: String): GameBoardState()
}