package com.demo.tictactoe.ui.gamehost

import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.JoinGameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class GameViewModelModule {

    @Provides
    fun provideHostGameUseCase(): HostGameUseCase {
        return Core.Host.hostGameUseCase
    }

    @Provides
    fun provideJoinGameUseCase(): JoinGameUseCase {
        return Core.Host.joinGameUseCase
    }

}