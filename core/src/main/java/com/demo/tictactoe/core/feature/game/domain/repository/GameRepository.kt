package com.demo.tictactoe.core.feature.game.domain.repository

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    //suspend fun connect(device: DeviceModel)
    suspend fun sendMove(move: Int)
    fun observeMoves(): Flow<Int>
    fun connectionState(): Flow<BluetoothConnectionState>
    fun disconnect()
}
