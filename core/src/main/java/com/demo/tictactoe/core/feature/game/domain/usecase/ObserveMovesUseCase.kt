package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveMovesUseCase(
    private val repository: GameRepository
) {
    suspend operator fun invoke(): Flow<Int> =
        repository.observeMoves()
}
