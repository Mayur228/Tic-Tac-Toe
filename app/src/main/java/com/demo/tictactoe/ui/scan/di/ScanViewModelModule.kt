package com.demo.tictactoe.ui.scan.di

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.scan.domain.usecase.JoinUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.ScanHostUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.StopConnectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ScanViewModelModule {

    @Provides
    fun provideJoinGameUseCase(): ScanHostUseCase {
        return Core.Join.scanHostUseCase
    }

    @Provides
    fun provideConnectToGameUseCase(): JoinUseCase {
        return Core.Join.joinGameUseCase
    }

    @Provides
    fun provideStopConnectionUseCase(): StopConnectionUseCase {
        return Core.Join.stopConnectionUseCase
    }
}