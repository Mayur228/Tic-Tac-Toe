package com.demo.tictactoe.core.feature.game.data.source

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GameSource {
    suspend fun sendMove(move: Int)
    fun observeMoves(): Flow<Int>
    fun connectionState(): Flow<BluetoothConnectionState>
    fun disconnect()
}

@Factory
class GameSourceImpl(
    private val bluetoothApi: BluetoothApi
) : GameSource {

    override suspend fun sendMove(move: Int) {
        bluetoothApi.sendMove(move)
    }

    override fun observeMoves(): Flow<Int> =
        bluetoothApi.incomingMoves

    override fun connectionState(): Flow<BluetoothConnectionState> =
        bluetoothApi.connectionState

    override fun disconnect() {
        bluetoothApi.disconnect()
    }
}

