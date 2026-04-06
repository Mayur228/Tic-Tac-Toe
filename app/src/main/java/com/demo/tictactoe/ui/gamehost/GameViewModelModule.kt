package com.demo.tictactoe.ui.gamehost

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
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