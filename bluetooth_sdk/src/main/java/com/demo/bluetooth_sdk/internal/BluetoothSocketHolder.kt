package com.demo.bluetooth_sdk.internal

import android.bluetooth.BluetoothSocket

internal class BluetoothSocketHolder {
    @Volatile
    var socket: BluetoothSocket? = null

    fun close() {
        try {
            socket?.close()
        } catch (_: Exception) {}
        socket = null
    }
}
