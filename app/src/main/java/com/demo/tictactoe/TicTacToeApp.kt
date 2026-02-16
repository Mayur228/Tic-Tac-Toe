package com.demo.tictactoe

import android.app.Application
import com.buildwithmayur.bluetooth.api.ClassicBluetoothSdk
import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.common.network.BluetoothApi
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TicTacToeApp : Application() {

    @Inject
    lateinit var bluetoothApi: BluetoothApi

    @Inject
    lateinit var provideActivity: FTAClass

    @Inject
    lateinit var bluetoothSdk: ClassicBluetoothSdk


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(provideActivity)
        bluetoothSdk = ClassicBluetoothSdk.initialize(applicationContext)

        initCore()
    }

    private fun initCore() {
        Core.init(
            config = Core.Config(
                bluetoothApi = bluetoothApi
            )
        )
    }
}
