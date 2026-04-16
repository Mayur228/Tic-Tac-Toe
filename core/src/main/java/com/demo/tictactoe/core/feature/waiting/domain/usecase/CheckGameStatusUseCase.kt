package com.demo.tictactoe.core.feature.waiting.domain.usecase

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import com.demo.tictactoe.core.feature.scan.domain.repository.JoinRepository
import com.demo.tictactoe.core.feature.waiting.domain.repository.WaitingRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class CheckGameStatusUseCase(private val repository: WaitingRepository) {
    suspend operator fun invoke(): Flow<BluetoothConnectionState> {
        return repository.checkGameStatus()
    }
}

