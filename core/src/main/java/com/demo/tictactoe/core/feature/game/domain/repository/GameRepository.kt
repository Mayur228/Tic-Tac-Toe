package com.demo.tictactoe.core.feature.game.domain.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun sendMove(move: Int): Resource<DataModel>
    suspend fun observeMoves(): Flow<Int>
    suspend fun connectionState(): Flow<BluetoothConnectionState>
    suspend fun disconnect(): Resource<Unit>
    suspend fun evaluateBoard(board: List<String>): Resource<GameResult>
}
