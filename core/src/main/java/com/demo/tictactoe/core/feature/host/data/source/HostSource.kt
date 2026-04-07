package com.demo.tictactoe.core.feature.host.data.source

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.ServerModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface HostSource {
    suspend fun startServer(hostName: String): Resource<ServerModel>
    fun connectionState(): Flow<BluetoothConnectionState>
}

@Factory
class HostSourceImpl(private val bluetoothApi: BluetoothApi): HostSource {
    override fun connectionState(): Flow<BluetoothConnectionState> =
        bluetoothApi.connectionState

    override suspend fun startServer(hostName: String): Resource<ServerModel> {
        return try {
            val data = bluetoothApi.startServer(hostName)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}