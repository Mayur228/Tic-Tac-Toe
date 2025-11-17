package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

@Factory
class HostGameUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(hostName: String): String? = repo.hostGame(hostName)
}
