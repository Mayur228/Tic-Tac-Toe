package com.demo.tictactoe.core.feature.host.domain.usecase

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import org.koin.core.annotation.Factory

@Factory
class JoinGameUseCase(private val repository: HostRepository){
    suspend operator fun invoke(hostName: String): List<DeviceModel> {
        return repository.joinGame(hostName)
    }
}
