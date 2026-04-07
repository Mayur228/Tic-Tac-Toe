package com.demo.tictactoe.core.feature.game.data.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.game.data.source.GameSource
import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GameRepositoryImpl(
    private val source: GameSource
) : GameRepository {

    override suspend fun sendMove(move: Int): Resource<DataModel> =
        source.sendMove(move)

    override suspend fun observeMoves(): Flow<Int> =
        source.observeMoves()

    override suspend fun connectionState(): Flow<BluetoothConnectionState> =
        source.connectionState()

    override suspend fun disconnect(): Resource<Unit> =
        source.disconnect()

    override suspend fun evaluateBoard(board: List<String>): Resource<GameResult> {
        return source.evaluateBoard(board = board)
    }

}
