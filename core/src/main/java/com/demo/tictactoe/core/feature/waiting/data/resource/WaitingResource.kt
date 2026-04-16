package com.demo.tictactoe.core.feature.waiting.data.resource

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface WaitingResource {
    suspend fun checkGameStatus(): Flow<BluetoothConnectionState>
}

@Factory
class WaitingResourceImpl(private val api: BluetoothApi): WaitingResource {
    override suspend fun checkGameStatus(): Flow<BluetoothConnectionState> {
        return api.connectionState
    }

}