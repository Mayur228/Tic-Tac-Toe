package com.demo.tictactoe.core.feature.scan.domain.usecase

import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import com.demo.tictactoe.core.feature.scan.domain.repository.JoinRepository
import org.koin.core.annotation.Factory

@Factory
class StopConnectionUseCase(
    private val joinRepository: JoinRepository
) {
    suspend operator fun invoke() {
        joinRepository.disconnect()
    }
}