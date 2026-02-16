package com.demo.tictactoe.core.feature.host.domain.repository

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    suspend fun hostGame(hostName: String)
    fun joinGame(hostName: String): Flow<DeviceModel>  // Flow for streaming devices
    fun connectionState(): Flow<BluetoothConnectionState>
}
