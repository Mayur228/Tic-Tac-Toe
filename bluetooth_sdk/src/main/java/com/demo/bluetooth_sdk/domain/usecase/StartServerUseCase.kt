package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

class StartServerUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(serverName: String) = repo.createServer(serverName)

    fun setOnClientConnected(cb: () -> Unit) {
        repo.onClientConnected(cb)
    }
}
