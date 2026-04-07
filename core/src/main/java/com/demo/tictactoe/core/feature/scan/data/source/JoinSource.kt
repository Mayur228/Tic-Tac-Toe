package com.demo.tictactoe.core.feature.scan.data.source

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface JoinSource {
    suspend fun discoverServers(hostName: String): Resource<Flow<DeviceModel>>
    suspend fun connectToDevice(device: DeviceModel): Resource<ConnectionModel>
    suspend fun disconnect(): Resource<Unit>
}

@Factory
class JoinSourceImp(private val api: BluetoothApi): JoinSource {
    override suspend fun discoverServers(hostName: String): Resource<Flow<DeviceModel>> {
        return try {
            val result = api.discoverServers(hostName)
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun connectToDevice(device: DeviceModel): Resource<ConnectionModel> {
        return try {
            val result = api.connect(device)
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }

    }

    override suspend fun disconnect(): Resource<Unit> {
        return try {
            val result = api.disconnect()
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}