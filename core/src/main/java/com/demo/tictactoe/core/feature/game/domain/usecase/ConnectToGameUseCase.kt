package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import org.koin.core.annotation.Factory

@Factory
class ConnectToGameUseCase(
    private val repository: GameRepository
) {
    suspend operator fun invoke(device: DeviceModel) {
        repository.connect(device)
    }
}
