package com.demo.tictactoe.ui.game

import com.demo.tictactoe.ui.AiDifficulty

data class GameBoardState(
    val board: List<String> = List(9) { "" },
    val myMark: String = "X",
    val isMyTurn: Boolean = false,
    val statusText: String = "Waiting…",

    val gameOver: Boolean = false,
    val winner: String? = null,
    val winningLine: List<Int>? = null,

    val isSinglePlayer: Boolean = false,
    val aiDifficulty: AiDifficulty = AiDifficulty.MEDIUM,

    val showFirstMoveDialog: Boolean = false,
    val isFirstMoveDecided: Boolean = false,
    val showDifficultyDialog: Boolean = false,
)