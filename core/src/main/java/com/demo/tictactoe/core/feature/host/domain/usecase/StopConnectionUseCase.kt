package com.demo.tictactoe.core.feature.host.domain.usecase

import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import org.koin.core.annotation.Factory

@Factory
class StopConnectionUseCase(
    private val gameRepository: GameRepository,
    private val hostRepository: HostRepository
) {
    suspend operator fun invoke() {
        gameRepository.disconnect()
        hostRepository.stop()
    }
}
