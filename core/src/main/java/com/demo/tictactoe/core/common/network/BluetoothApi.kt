package com.demo.tictactoe.core.common.network

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.ServerModel
import kotlinx.coroutines.flow.Flow

interface BluetoothApi {
    val connectionState: Flow<BluetoothConnectionState>
    suspend fun discoverServers(serverName: String): Flow<DeviceModel>
    suspend fun startServer(serverName: String): ServerModel
    suspend fun connect(device: DeviceModel): ConnectionModel
    suspend fun sendMove(move: Int): DataModel
    val incomingMoves: Flow<Int>
    suspend fun disconnect()
}
