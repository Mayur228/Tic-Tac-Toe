package com.demo.bluetooth_sdk.domain.usecase

import kotlinx.coroutines.flow.Flow

class Understanding {
}

data class GameAction(
    val row: Int,
    val column: Int,
    val player: GamePlayers
)

enum class GamePlayers {
    X,
    O
}

data class GameState(
    val playedActions: List<GameAction>,
    val currentPlayer: GamePlayers,
    val winner: GamePlayers,
    val winningStep: List<GameAction>,
)

interface TicTacToeGameInterface {
    suspend fun doAction(action: GameAction)
    suspend fun getCurrentGameState(): Flow<GameState>
}