package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import org.koin.core.annotation.Factory

@Factory
class SendMoveUseCase(
    private val repository: GameRepository
) {
    suspend operator fun invoke(move: Int): Resource<DataModel> {
        return repository.sendMove(move)
    }
}
