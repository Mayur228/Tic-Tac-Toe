package com.demo.tictactoe.ui.waiting.di

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.scan.domain.usecase.JoinUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.ScanHostUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.StopConnectionUseCase
import com.demo.tictactoe.core.feature.waiting.domain.usecase.CheckGameStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class WaitingViewModelModule {

    @Provides
    fun provideCheckGameStatusUseCase(): CheckGameStatusUseCase {
        return Core.Waiting.checkGameStatusUseCase
    }
}