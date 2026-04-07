package com.demo.tictactoe.core.feature.host.data.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.ServerModel
import com.demo.tictactoe.core.feature.host.data.source.HostSource
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory

@Factory
class HostRepositoryImpl(private val source: HostSource) : HostRepository {

    override suspend fun hostGame(hostName: String): Resource<ServerModel> {
        return source.startServer(hostName)
    }

    override suspend fun connectionState(): Flow<BluetoothConnectionState> =
        source.connectionState()

}
