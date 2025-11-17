package com.demo.bluetooth_sdk

import android.bluetooth.BluetoothManager
import android.content.Context
import com.demo.bluetooth_sdk.di.BluetoothModule
import com.demo.bluetooth_sdk.domain.usecase.ConnectUseCase
import com.demo.bluetooth_sdk.domain.usecase.HostGameUseCase
import com.demo.bluetooth_sdk.domain.usecase.JoinGameUseCase
import com.demo.bluetooth_sdk.domain.usecase.ObserveMovesUseCase
import com.demo.bluetooth_sdk.domain.usecase.SendMoveUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartScanUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartServerUseCase
import com.demo.bluetooth_sdk.sdk.GameBluetoothSdk
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

object BluetoothCore {

    data class Config(
        val appContext: Context,
        val bluetoothManager: BluetoothManager
    )

    fun init(config: Config) {
        startKoin {
            modules(
                // Koin annotation module (KSP)
                BluetoothModule().module,

                // pass AppContext to the SDK
                module {
                    single { config.appContext }
                    single { config.bluetoothManager.adapter }
                }
            )
        }
    }

    object Sdk {
        val gameBluetoothSdk: GameBluetoothSdk
            get() = get().get()
    }

    object Bluetooth {
        val hostGameUseCase: HostGameUseCase
            get() = get().get()

        val joinGameUseCase: JoinGameUseCase
            get() = get().get()

        val startScanUseCase: StartScanUseCase
            get() = get().get()

        val sendMoveUseCase: SendMoveUseCase
            get() = get().get()

        val observeMovesUseCase: ObserveMovesUseCase
            get() = get().get()

    }
}
