package com.demo.tictactoe.ui.game.di

import com.demo.tictactoe.core.Core
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.JoinUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.ScanHostUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.StopConnectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class GameBoardViewModelModule {

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