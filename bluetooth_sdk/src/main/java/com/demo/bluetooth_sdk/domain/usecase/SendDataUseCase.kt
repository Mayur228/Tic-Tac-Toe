package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

class SendDataUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(move: Int) = repo.sendData(move)
}
