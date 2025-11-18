package com.demo.tictactoe

import android.app.Application
import com.demo.bluetooth_sdk.BluetoothClassicSdk
import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
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


    override fun onCreate() {
        super.onCreate()
//        BluetoothClassicSdk.init(
//            context = applicationContext
//        )
        registerActivityLifecycleCallbacks(provideActivity)
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
