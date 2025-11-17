package com.demo.bluetooth_sdk.domain.usecase

import android.bluetooth.BluetoothDevice
import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

@Factory
class JoinGameUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(device: BluetoothDevice): Boolean {
        return repo.connectTo(device)
    }
}
