package com.demo.tictactoe.core.feature.host.domain.usecase

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ServerModel
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import org.koin.core.annotation.Factory

@Factory
class HostGameUseCase(private val repository: HostRepository){
    suspend operator fun invoke(hostName: String): Resource<ServerModel> {
        return repository.hostGame(hostName)
    }
}
