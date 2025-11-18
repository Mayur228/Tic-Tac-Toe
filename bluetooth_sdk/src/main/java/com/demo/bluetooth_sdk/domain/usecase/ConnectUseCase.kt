package com.demo.bluetooth_sdk.domain.usecase

import android.bluetooth.BluetoothDevice
import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

class ConnectUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(device: BluetoothDevice) = repo.connectToDevice(device)
}
