package com.demo.tictactoe.core.feature.host.data.source

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import org.koin.core.annotation.Factory

interface HostSource {
    suspend fun hostGame(hostName: String): Unit
    suspend fun joinGame(hostName: String): List<DeviceModel>
}

@Factory
class HostSourceImpl(private val bluetoothApi: BluetoothApi): HostSource {
    override suspend fun hostGame(hostName: String) {
        return bluetoothApi.createServer(hostName)
    }

    override suspend fun joinGame(hostName: String): List<DeviceModel> {
        return bluetoothApi.discoverServer(hostName)
    }

}