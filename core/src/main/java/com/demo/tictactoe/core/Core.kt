package com.demo.tictactoe.core

import com.demo.tictactoe.core.common.di.CoreModule
import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.JoinUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.ScanHostUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.StopConnectionUseCase
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

        /*val stopConnectionUseCase: StopConnectionUseCase
            get() = get().get()*/

    }

    object Join {
        val joinGameUseCase: JoinUseCase
            get() = get().get()

        val scanHostUseCase: ScanHostUseCase
            get() = get().get()

        val stopConnectionUseCase: StopConnectionUseCase
            get() = get().get()
    }

    object Game {
        val ObserveMovesUseCase: ObserveMovesUseCase
            get() = get().get()

        val SendMoveUseCase: SendMoveUseCase
            get() = get().get()

        val evaluateBoardUseCase: EvaluateBoardUseCase
            get() = get().get()
    }
}