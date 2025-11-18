package com.demo.tictactoe.core.feature.host.domain.repository

import com.demo.tictactoe.core.common.model.DeviceModel

interface HostRepository {
    suspend fun hostGame(hostName: String): Unit
    suspend fun joinGame(hostName: String): List<DeviceModel>
}