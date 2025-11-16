package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

@Factory
class StartServerUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke() = repo.startServer()

    fun setOnClientConnected(cb: () -> Unit) {
        repo.setOnClientConnected(cb)
    }
}
