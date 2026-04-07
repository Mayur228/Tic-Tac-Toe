package com.demo.tictactoe.core.feature.game.data.model

sealed class GameResult {
    data class Win(
        val winner: String,
        val winningLine: List<Int>
    ) : GameResult()
    object Draw : GameResult()
    object Ongoing : GameResult()
}

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD
}