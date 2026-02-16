package com.demo.tictactoe.core.feature.host.data.source

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface HostSource {
    fun connectionState(): Flow<BluetoothConnectionState>
    fun discoverServers(hostName: String): Flow<DeviceModel>
    suspend fun startServer(hostName: String)
    suspend fun connectToDevice(device: DeviceModel)
}

@Factory
class HostSourceImpl(private val bluetoothApi: BluetoothApi): HostSource {
    override fun connectionState(): Flow<BluetoothConnectionState> =
        bluetoothApi.connectionState

    override fun discoverServers(hostName: String): Flow<DeviceModel> =
        bluetoothApi.discoverServers(hostName)

    override suspend fun startServer(hostName: String) {
        bluetoothApi.startServer(hostName)
    }

    override suspend fun connectToDevice(device: DeviceModel) {
        bluetoothApi.connect(device)
    }
}