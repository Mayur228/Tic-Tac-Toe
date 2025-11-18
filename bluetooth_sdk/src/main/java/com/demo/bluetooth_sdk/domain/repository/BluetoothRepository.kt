package com.demo.bluetooth_sdk.domain.repository

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    fun discoverAllDevice(): Flow<BluetoothDevice>
    fun discoverHostDevice(serverName: String): Flow<BluetoothDevice>
    suspend fun createServer(serverName: String): String?
    suspend fun connectToDevice(device: BluetoothDevice): Boolean
    suspend fun sendData(data: Int)
    val incomingData: Flow<Int>
    fun onClientConnected(cb: () -> Unit)
}
