package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

class StartScanUseCase(private val repo: BluetoothRepository) {
//    operator fun invoke(serverName: String) = repo.discoverHostDevice(serverName = serverName)
    operator fun invoke(serverName: String) = repo.discoverAllDevice()
}
