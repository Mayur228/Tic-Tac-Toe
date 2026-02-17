package com.demo.tictactoe.ui.gamehost

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.game.domain.usecase.ConnectToGameUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
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

    @Provides
    fun provideConnectToGameUseCase(): ConnectToGameUseCase {
        return Core.Game.ConnectToGameUseCase
    }

    @Provides
    fun provideObserveMoveUseCase(): ObserveMovesUseCase{
        return Core.Game.ObserveMovesUseCase
    }

    @Provides
    fun provideSendMoveUseCase(): SendMoveUseCase {
        return Core.Game.SendMoveUseCase
    }

    @Provides
    fun provideEvaluateBoardUseCase(): EvaluateBoardUseCase {
        return Core.Game.evaluateBoardUseCase
    }

}