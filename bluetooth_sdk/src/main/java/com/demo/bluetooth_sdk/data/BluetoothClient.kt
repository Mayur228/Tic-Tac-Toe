package com.demo.bluetooth_sdk.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class BluetoothClient(
    private val context: android.content.Context,
    private val adapter: BluetoothAdapter?,
    private val incomingMoves: MutableSharedFlow<Int>,
    private val sharedSocketRef: () -> BluetoothSocket?,
    private val setSocket: (BluetoothSocket) -> Unit
) {

    private val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    @SuppressLint("MissingPermission")
    suspend fun connectTo(device: BluetoothDevice): Boolean = withContext(Dispatchers.IO) {
        try {
            adapter?.cancelDiscovery()
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
            setSocket(socket)
            startReader(socket)
            true
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun sendMove(move: Int) {
        withContext(Dispatchers.IO) {
            try {
                sharedSocketRef()?.outputStream?.write(byteArrayOf(move.toByte()))
            } catch (_: IOException) {
                // ignore write error
            }
        }
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