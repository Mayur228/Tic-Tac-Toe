package com.demo.tictactoe.core.feature.scan.domain.usecase

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import com.demo.tictactoe.core.feature.scan.domain.repository.JoinRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class JoinUseCase(private val repository: JoinRepository) {
    suspend operator fun invoke(device: DeviceModel): Resource<ConnectionModel> {
        return repository.connectToDevice(device = device)
    }
}

