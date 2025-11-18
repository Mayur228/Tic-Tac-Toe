package com.demo.bluetooth_sdk

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.demo.bluetooth_sdk.domain.usecase.ConnectUseCase
import com.demo.bluetooth_sdk.domain.usecase.ObserveMovesUseCase
import com.demo.bluetooth_sdk.domain.usecase.SendDataUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartScanUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartServerUseCase
import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
import org.koin.core.context.GlobalContext.get

object BluetoothClassicSdk {

    fun buildBluetoothSdk(context: Context): ClassicBluetoothManager {
        return ClassicBluetoothManager()
    }

   /* fun init(context: Context) {
        startKoin {
            modules(
                BluetoothModule().module,
                module {
                    single { context }
                    single { (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter }
                }
            )
        }
    }*/

    /*fun getModules(context: Context) = listOf(
        BluetoothModule().module,
        module {
            single { context }
            single { (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter }
        }
    )*/


    /*object Manager {
        val bluetoothSdkManager: ClassicBluetoothManager
            get() = get().get()
    }

    object Bluetooth {
        val connectUseCase: ConnectUseCase
            get() = get().get()

        val startScanUseCase: StartScanUseCase
            get() = get().get()

        val startServerUseCase: StartServerUseCase
            get() = get().get()

        val sendDataUseCase: SendDataUseCase
            get() = get().get()

        val observeMovesUseCase: ObserveMovesUseCase
            get() = get().get()

    }*/
}
