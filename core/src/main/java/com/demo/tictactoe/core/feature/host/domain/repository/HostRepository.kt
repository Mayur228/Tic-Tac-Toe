package com.demo.tictactoe.core.feature.host.domain.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.ServerModel
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    suspend fun hostGame(hostName: String): Resource<ServerModel>
    suspend fun connectionState(): Flow<BluetoothConnectionState>
}
