package com.demo.bluetooth_sdk.internal

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import com.demo.bluetooth_sdk.api.BluetoothPeer
import com.demo.bluetooth_sdk.internal.permission.BluetoothPermissionChecker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect

internal class BluetoothScanner(
    private val context: Context,
    private val adapter: BluetoothAdapter?
) {

    @SuppressLint("MissingPermission")
    fun scanAll(): Flow<BluetoothPeer> = callbackFlow {
        // Permission check
        if (!BluetoothPermissionChecker.hasScanPermission(context)) {
            close()
            return@callbackFlow
        }

        if (adapter == null || !adapter.isEnabled) {
            close()
            return@callbackFlow
        }

        // Bluetooth device receiver
        val receiver = BluetoothReceiver { device ->
            // Map BluetoothDevice → BluetoothPeer
            val peer = BluetoothPeer(
                name = device.name,
                address = device.address
            )
            trySend(peer)
        }

        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        adapter.startDiscovery()

        awaitClose {
            try { context.unregisterReceiver(receiver) } catch (_: Exception) {}
            adapter.cancelDiscovery()
        }
    }

    @SuppressLint("MissingPermission")
    fun scanHost(serverName: String): Flow<BluetoothPeer> = callbackFlow {
        scanAll().collect { peer ->
            if (peer.name?.startsWith(serverName, ignoreCase = true) == true) {
                trySend(peer)
            }
        }
    }
}
