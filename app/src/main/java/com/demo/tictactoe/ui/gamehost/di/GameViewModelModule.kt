package com.demo.tictactoe.ui.gamehost.di

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
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
}