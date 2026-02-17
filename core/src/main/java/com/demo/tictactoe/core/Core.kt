package com.demo.tictactoe.core

import com.demo.tictactoe.core.common.di.CoreModule
import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.core.feature.game.domain.usecase.ConnectToGameUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.JoinGameUseCase
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

object Core {

    data class Config(
        val bluetoothApi: BluetoothApi
    )

    fun init(config: Config) {
        startKoin {
            modules(
                CoreModule().module,
                module {
                    single {
                        config.bluetoothApi
                    }
                }
            )
        }
    }

    object Host {
        val hostGameUseCase: HostGameUseCase
            get() = get().get()

        val joinGameUseCase: JoinGameUseCase
            get() = get().get()

    }

    object Game {
        val ConnectToGameUseCase: ConnectToGameUseCase
            get() = get().get()

        val ObserveMovesUseCase: ObserveMovesUseCase
            get() = get().get()

        val SendMoveUseCase: SendMoveUseCase
            get() = get().get()

        val evaluateBoardUseCase: EvaluateBoardUseCase
            get() = get().get()
    }
}