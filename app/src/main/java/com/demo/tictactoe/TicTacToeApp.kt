package com.demo.tictactoe

import android.app.Application
import android.bluetooth.BluetoothManager
import com.demo.bluetooth_sdk.BluetoothCore
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TicTacToeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val bluetoothManager = getSystemService(BluetoothManager::class.java)
            ?: throw IllegalStateException("BluetoothManager not available")

        BluetoothCore.init(
            BluetoothCore.Config(
                appContext = applicationContext,
                bluetoothManager = bluetoothManager
            )
        )
    }
}
