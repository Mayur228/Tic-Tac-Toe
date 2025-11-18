package com.demo.bluetooth_sdk.sdk

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.demo.bluetooth_sdk.BluetoothClassicSdk.Bluetooth.connectUseCase
import com.demo.bluetooth_sdk.BluetoothClassicSdk.Bluetooth.observeMovesUseCase
import com.demo.bluetooth_sdk.BluetoothClassicSdk.Bluetooth.sendDataUseCase
import com.demo.bluetooth_sdk.BluetoothClassicSdk.Bluetooth.startScanUseCase
import com.demo.bluetooth_sdk.BluetoothClassicSdk.Bluetooth.startServerUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

class ClassicBluetoothManager() {

    fun startScan(serverName: String) = startScanUseCase(serverName = serverName)

    suspend fun hostServer(serverName: String): String? = startServerUseCase(serverName)

    suspend fun connectToDevice(device: BluetoothDevice) = connectUseCase(device)

    suspend fun sendData(position: Int) = sendDataUseCase(position)

    suspend fun receiveData(): Flow<Int> = observeMovesUseCase()

    // optional client connected callback wiring — StartServerUseCase should wire repository callback
    fun onClientConnected(cb: () -> Unit) {
        // try to call a usecase-level setter if implemented
        try { startServerUseCase.setOnClientConnected(cb) } catch (_: Throwable) {}
    }
}