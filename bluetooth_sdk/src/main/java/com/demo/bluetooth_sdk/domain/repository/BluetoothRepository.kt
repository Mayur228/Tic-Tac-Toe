package com.demo.bluetooth_sdk.domain.repository

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    suspend fun hostGame(hostName: String): String?
    fun startScan(): Flow<BluetoothDevice>
    suspend fun startServer(): String?
    suspend fun connectTo(device: BluetoothDevice): Boolean
    suspend fun sendMove(move: Int)
    val incomingMoves: Flow<Int>
    fun setOnClientConnected(cb: () -> Unit)
}
