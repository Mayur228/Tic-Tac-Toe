package com.demo.bluetooth_sdk.internal

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
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

internal class BluetoothServer(
    private val context: Context,
    private val adapter: BluetoothAdapter?,
    private val socketHolder: BluetoothSocketHolder,
    private val incoming: MutableSharedFlow<Int>,
    private val stateFlow: MutableStateFlow<ConnectionState>
) {

    private val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    @SuppressLint("MissingPermission")
    suspend fun start(serverName: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (!BluetoothPermissionChecker.hasConnectPermission(context)) {
            stateFlow.value = ConnectionState.Error(BluetoothError.PermissionDenied)
            return@withContext Result.failure(Exception("Missing advertise permission"))
        }

        if (adapter == null || !adapter.isEnabled) {
            stateFlow.value = ConnectionState.Error(BluetoothError.BluetoothDisabled)
            return@withContext Result.failure(Exception("Bluetooth disabled"))
        }

        stateFlow.value = ConnectionState.Connecting

        return@withContext try {
            adapter.name = serverName
            val serverSocket = adapter.listenUsingRfcommWithServiceRecord("BluetoothSDK", uuid)
            val socket = serverSocket.accept()
            socketHolder.socket = socket
            readLoop(socket)
            stateFlow.value = ConnectionState.Connected(socket.remoteDevice.name)
            Result.success(Unit)
        } catch (e: IOException) {
            stateFlow.value = ConnectionState.Error(BluetoothError.ConnectionFailed)
            Result.failure(e)
        } catch (e: Exception) {
            stateFlow.value = ConnectionState.Error(BluetoothError.Unknown(e))
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
