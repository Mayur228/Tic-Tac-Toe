package com.demo.bluetooth_sdk.data

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothScanner(
    private val context: Context,
    private val adapter: BluetoothAdapter?
) {

    private fun hasScanPermission(): Boolean {
        val perm = Manifest.permission.BLUETOOTH_SCAN
        return ContextCompat.checkSelfPermission(context, perm) ==
                PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun startScan(): Flow<BluetoothDevice> = callbackFlow {
        if (!hasScanPermission() || adapter == null || !adapter.isEnabled) {
            close()
            return@callbackFlow
        }

        val receiver = BluetoothReceiver { device ->
            trySend(device)
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)

        try {
            adapter.startDiscovery()
        } catch (_: SecurityException) {
            close()
        }

        awaitClose {
            try { context.unregisterReceiver(receiver) } catch (_: Exception) {}
            adapter.cancelDiscovery()
        }
    }
}