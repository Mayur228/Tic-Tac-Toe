package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import org.koin.core.annotation.Factory

@Factory
class EvaluateBoardUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(board: List<String>): Resource<GameResult> {
        return repository.evaluateBoard(board = board)
    }
}
