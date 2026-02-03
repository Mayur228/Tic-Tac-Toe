package com.demo.bluetooth_sdk.internal

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class BluetoothReceiver(
    private val onFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {

    private val discovered = HashSet<String>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != BluetoothDevice.ACTION_FOUND) return

        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        device?.let {
            if (discovered.add(it.address)) {
                onFound(it)
            }
        }
    }
}
