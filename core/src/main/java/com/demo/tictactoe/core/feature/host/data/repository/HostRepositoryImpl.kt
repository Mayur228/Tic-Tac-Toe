package com.demo.tictactoe.core.feature.host.data.repository

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.host.data.source.HostSource
import com.demo.tictactoe.core.feature.host.domain.repository.HostRepository
import org.koin.core.annotation.Factory

@Factory
class HostRepositoryImpl(private val source: HostSource): HostRepository {
    override suspend fun hostGame(hostName: String) {
        return source.hostGame(hostName)
    }

    override suspend fun joinGame(hostName: String): List<DeviceModel> {
        return source.joinGame(hostName)
    }
}