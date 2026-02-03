package com.demo.tictactoe.core.common.network

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import kotlinx.coroutines.flow.Flow

interface BluetoothApi {
    val connectionState: Flow<BluetoothConnectionState>
    fun discoverServers(serverName: String): Flow<DeviceModel>
    suspend fun startServer(serverName: String)
    suspend fun connect(device: DeviceModel)
    suspend fun sendMove(move: Int)
    val incomingMoves: Flow<Int>
    fun disconnect()
}
