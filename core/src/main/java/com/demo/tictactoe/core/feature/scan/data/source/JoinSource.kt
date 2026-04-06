package com.demo.tictactoe.core.feature.scan.data.source

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface JoinSource {
    suspend fun discoverServers(hostName: String): Flow<DeviceModel>
    suspend fun connectToDevice(device: DeviceModel)
    suspend fun disconnect()
}

@Factory
class JoinSourceImp(private val api: BluetoothApi): JoinSource {
    override suspend fun discoverServers(hostName: String): Flow<DeviceModel> {
        return api.discoverServers(hostName)
    }

    override suspend fun connectToDevice(device: DeviceModel) {
        return api.connect(device)
    }

    override suspend fun disconnect() {
        return api.disconnect()
    }
}