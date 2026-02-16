package com.demo.tictactoe.core.feature.host.data.repository

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.host.data.source.HostSource
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory

@Factory
class HostRepositoryImpl(private val source: HostSource) : HostRepository {

    private val _discoveredDevices = mutableSetOf<String>() // cache by address

    override suspend fun hostGame(hostName: String) {
        source.startServer(hostName)
    }

    override fun joinGame(hostName: String): Flow<DeviceModel> =
        source.discoverServers(hostName).onEach { device ->
            // Optional: cache logic to avoid duplicates
            if (_discoveredDevices.add(device.address)) {
                // new device discovered
            }
        }

    override fun connectionState(): Flow<BluetoothConnectionState> =
        source.connectionState()
}
