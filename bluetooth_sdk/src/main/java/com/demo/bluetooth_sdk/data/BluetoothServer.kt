package com.demo.bluetooth_sdk.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class BluetoothServer(
    private val adapter: BluetoothAdapter?,
    private val incomingMoves: MutableSharedFlow<Int>,
    private val setSocket: (BluetoothSocket) -> Unit,
    private val onClientConnected: (() -> Unit)? = null
) {

    private val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    @SuppressLint("MissingPermission")
    suspend fun createServer(serverName: String): String? = withContext(Dispatchers.IO) {
        adapter?.name = serverName
        val serverSocket: BluetoothServerSocket =
            adapter?.listenUsingRfcommWithServiceRecord("GameBluetooth", uuid) ?: return@withContext null

        val socket = try {
            serverSocket.accept()
        } catch (e: IOException) {
            return@withContext null
        }

        // notify immediately
        onClientConnected?.invoke()

        setSocket(socket)
        startReader(socket)
        return@withContext socket.remoteDevice.name
    }

    private suspend fun startReader(socket: BluetoothSocket) {
        withContext(Dispatchers.IO) {
            val input = socket.inputStream
            val buffer = ByteArray(1)
            while (true) {
                try {
                    val read = input.read(buffer)
                    if (read > 0) incomingMoves.emit(buffer[0].toInt())
                } catch (_: IOException) {
                    break
                }
            }
        }
    }
}