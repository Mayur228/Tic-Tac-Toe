package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

@Factory
class StartScanUseCase(private val repo: BluetoothRepository) {
    operator fun invoke() = repo.startScan()
}
