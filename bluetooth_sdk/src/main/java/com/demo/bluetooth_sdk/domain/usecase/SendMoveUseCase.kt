package com.demo.bluetooth_sdk.domain.usecase

import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import org.koin.core.annotation.Factory

@Factory
class SendMoveUseCase(private val repo: BluetoothRepository) {
    suspend operator fun invoke(move: Int) = repo.sendMove(move)
}
