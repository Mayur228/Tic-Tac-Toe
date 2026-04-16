package com.demo.tictactoe.core.feature.waiting.data.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.feature.game.data.source.GameSourceImpl
import com.demo.tictactoe.core.feature.waiting.data.resource.WaitingResource
import com.demo.tictactoe.core.feature.waiting.domain.repository.WaitingRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class WaitingRepositoryImpl(private val sourceImpl: WaitingResource): WaitingRepository {
    override suspend fun checkGameStatus(): Flow<BluetoothConnectionState> {
        return sourceImpl.checkGameStatus()
    }
}