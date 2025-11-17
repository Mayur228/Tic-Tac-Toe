package com.demo.tictactoe.ui.gamehost

import com.demo.bluetooth_sdk.BluetoothCore
import com.demo.bluetooth_sdk.domain.usecase.HostGameUseCase
import com.demo.bluetooth_sdk.domain.usecase.JoinGameUseCase
import com.demo.bluetooth_sdk.domain.usecase.ObserveMovesUseCase
import com.demo.bluetooth_sdk.domain.usecase.SendMoveUseCase
import com.demo.bluetooth_sdk.domain.usecase.StartScanUseCase
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
    fun provideSendMoveUseCase(): SendMoveUseCase {
        return BluetoothCore.Bluetooth.sendMoveUseCase
    }

    @Provides
    fun provideHostGameUseCase(): HostGameUseCase {
        return BluetoothCore.Bluetooth.hostGameUseCase
    }

    @Provides
    fun provideJoinGameUseCase(): JoinGameUseCase {
        return BluetoothCore.Bluetooth.joinGameUseCase
    }

    @Provides
    fun provideObserveMovesUseCase(): ObserveMovesUseCase {
        return BluetoothCore.Bluetooth.observeMovesUseCase
    }

    @Provides
    fun provideSdk(): GameBluetoothSdk {
        return GameBluetoothSdk(
            hostGameUseCase = provideHostGameUseCase(),
            joinGameUseCase = provideJoinGameUseCase(),
            startScanUseCase = provideStartScanUseCase(),
            sendMoveUseCase = provideSendMoveUseCase(),
            observeMovesUseCase = provideObserveMovesUseCase()
        )
    }
}