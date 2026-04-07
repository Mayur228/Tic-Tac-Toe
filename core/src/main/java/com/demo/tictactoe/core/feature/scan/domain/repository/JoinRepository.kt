package com.demo.tictactoe.core.feature.scan.domain.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DeviceModel
import kotlinx.coroutines.flow.Flow

interface JoinRepository {
    suspend fun discoverServers(hostName: String): Resource<Flow<DeviceModel>>
    suspend fun connectToDevice(device: DeviceModel): Resource<ConnectionModel>
    suspend fun disconnect(): Resource<Unit>
}
