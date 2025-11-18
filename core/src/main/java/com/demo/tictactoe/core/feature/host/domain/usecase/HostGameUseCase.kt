package com.demo.tictactoe.core.feature.host.domain.usecase

import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import org.koin.core.annotation.Factory

@Factory
class HostGameUseCase(private val repository: HostRepository){
    suspend operator fun invoke(hostName: String): Unit {
        return repository.hostGame(hostName)
    }
}
