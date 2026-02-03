package com.demo.bluetooth_sdk.internal

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.demo.bluetooth_sdk.api.BluetoothError
import com.demo.bluetooth_sdk.api.ConnectionState
import com.demo.bluetooth_sdk.internal.permission.BluetoothPermissionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

internal class BluetoothClient(
    private val context: Context,
    private val adapter: BluetoothAdapter?,
    private val socketHolder: BluetoothSocketHolder,
    private val incoming: MutableSharedFlow<Int>,
    private val stateFlow: MutableStateFlow<ConnectionState>
) {

    private val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    @SuppressLint("MissingPermission")
    suspend fun connect(device: BluetoothDevice): Result<Unit> = withContext(Dispatchers.IO) {

        if (!BluetoothPermissionChecker.hasConnectPermission(context)) {
            stateFlow.value = ConnectionState.Error(BluetoothError.PermissionDenied)
            return@withContext Result.failure(Exception("Missing connect permission"))
        }

        stateFlow.value = ConnectionState.Connecting

        return@withContext try {
            adapter?.cancelDiscovery()
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
            socketHolder.socket = socket
            readLoop(socket)
            stateFlow.value = ConnectionState.Connected(socket.remoteDevice.name)
            Result.success(Unit)
        } catch (e: Exception) {
            stateFlow.value = ConnectionState.Error(BluetoothError.ConnectionFailed)
            Result.failure(e)
        }
    }

    suspend fun send(data: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            socketHolder.socket?.outputStream?.write(byteArrayOf(data.toByte()))
            Result.success(Unit)
        } catch (e: IOException) {
            stateFlow.value = ConnectionState.Error(BluetoothError.ConnectionFailed)
            Result.failure(e)
        }
    }

    private suspend fun readLoop(socket: android.bluetooth.BluetoothSocket) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1)
            try {
                while (socket.isConnected) {
                    val read = socket.inputStream.read(buffer)
                    if (read > 0) incoming.emit(buffer[0].toInt())
                }
            } catch (_: IOException) {
                stateFlow.value = ConnectionState.Disconnected
            }
        }
    }
}
