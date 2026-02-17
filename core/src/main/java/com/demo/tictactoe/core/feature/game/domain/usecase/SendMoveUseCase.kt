package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import org.koin.core.annotation.Factory

@Factory
class SendMoveUseCase(
    private val repository: GameRepository
) {
    suspend operator fun invoke(move: Int) {
        repository.sendMove(move)
    }
}
