package com.demo.bluetooth_sdk.sdk

import android.bluetooth.BluetoothDevice
import com.demo.bluetooth_sdk.domain.usecase.StartScanUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartServerUseCase
import com.demo.bluetooth_sdk.domain.usecase.ConnectUseCase
import com.demo.bluetooth_sdk.domain.usecase.ObserveMovesUseCase
import com.demo.bluetooth_sdk.domain.usecase.SendMoveUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GameBluetoothSdk(
    private val startScanUseCase: StartScanUseCase,
    private val startServerUseCase: StartServerUseCase,
    private val connectUseCase: ConnectUseCase,
    private val sendMoveUseCase: SendMoveUseCase,
    private val observeMovesUseCase: ObserveMovesUseCase
) {

    fun scan() = startScanUseCase()

    suspend fun waitForPlayer(): String? = startServerUseCase()

    suspend fun connect(device: BluetoothDevice) = connectUseCase(device)

    suspend fun sendMove(position: Int) = sendMoveUseCase(position)

    val incomingMoves: Flow<Int> = observeMovesUseCase()

    // optional client connected callback wiring — StartServerUseCase should wire repository callback
    fun setOnClientConnected(cb: () -> Unit) {
        // try to call a usecase-level setter if implemented
        try { startServerUseCase.setOnClientConnected(cb) } catch (_: Throwable) {}
    }
}