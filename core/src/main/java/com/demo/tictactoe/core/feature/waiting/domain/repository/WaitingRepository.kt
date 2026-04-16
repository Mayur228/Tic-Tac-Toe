package com.demo.tictactoe.core.feature.waiting.domain.repository

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import kotlinx.coroutines.flow.Flow

interface WaitingRepository {
    suspend fun checkGameStatus(): Flow<BluetoothConnectionState>
}