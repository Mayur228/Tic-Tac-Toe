package com.demo.tictactoe.core.feature.game.data.repository

import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.game.data.source.GameSource
import com.demo.tictactoe.core.feature.game.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GameRepositoryImpl(
    private val source: GameSource
) : GameRepository {

    override suspend fun connect(device: DeviceModel) =
        source.connect(device)

    override suspend fun sendMove(move: Int) =
        source.sendMove(move)

    override fun observeMoves(): Flow<Int> =
        source.observeMoves()

    override fun connectionState(): Flow<BluetoothConnectionState> =
        source.connectionState()

    override fun disconnect() =
        source.disconnect()
}
