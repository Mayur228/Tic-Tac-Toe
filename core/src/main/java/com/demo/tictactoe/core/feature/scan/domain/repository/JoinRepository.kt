package com.demo.tictactoe.core.feature.scan.domain.repository

import com.demo.tictactoe.core.common.model.DeviceModel
import kotlinx.coroutines.flow.Flow

interface JoinRepository {
    suspend fun discoverServers(hostName: String): Flow<DeviceModel>
    suspend fun connectToDevice(device: DeviceModel)
    suspend fun disconnect()
}
