package com.demo.tictactoe.core.feature.scan.data.repository

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.scan.data.source.JoinSource
import com.demo.tictactoe.core.feature.scan.domain.repository.JoinRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class JoinRepositoryImpl(private val source: JoinSource): JoinRepository {
    override suspend fun discoverServers(hostName: String): Flow<DeviceModel> {
        return source.discoverServers(hostName = hostName)
    }

    override suspend fun connectToDevice(device: DeviceModel) {
        return source.connectToDevice(device = device)
    }

    override suspend fun disconnect() {
        return source.disconnect()
    }
}