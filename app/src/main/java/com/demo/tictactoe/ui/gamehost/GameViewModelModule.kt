package com.demo.tictactoe.ui.gamehost

import com.demo.bluetooth_sdk.BluetoothCore
import com.demo.bluetooth_sdk.domain.usecase.ConnectUseCase
import com.demo.bluetooth_sdk.domain.usecase.ObserveMovesUseCase
import com.demo.bluetooth_sdk.domain.usecase.SendMoveUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartScanUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartServerUseCase
import com.demo.bluetooth_sdk.sdk.GameBluetoothSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class GameViewModelModule {

    @Provides
    fun provideStartScanUseCase(): StartScanUseCase {
        return BluetoothCore.Bluetooth.startScanUseCase
    }

    @Provides
    fun provideStartServerUseCase(): StartServerUseCase {
        return BluetoothCore.Bluetooth.startServerUseCase
    }

    @Provides
    fun provideConnectUseCase(): ConnectUseCase {
        return BluetoothCore.Bluetooth.connectUseCase
    }

    @Provides
    fun provideSendMoveUseCase(): SendMoveUseCase {
        return BluetoothCore.Bluetooth.sendMoveUseCase
    }

    @Provides
    fun provideObserveMovesUseCase(): ObserveMovesUseCase {
        return BluetoothCore.Bluetooth.observeMovesUseCase
    }

    @Provides
    fun provideSdk(): GameBluetoothSdk {
        return GameBluetoothSdk(
            startScanUseCase = provideStartScanUseCase(),
            startServerUseCase = provideStartServerUseCase(),
            connectUseCase = provideConnectUseCase(),
            sendMoveUseCase = provideSendMoveUseCase(),
            observeMovesUseCase = provideObserveMovesUseCase()
        )
    }
}