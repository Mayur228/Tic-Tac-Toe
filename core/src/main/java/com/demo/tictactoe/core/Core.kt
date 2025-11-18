package com.demo.tictactoe.core

import com.demo.tictactoe.core.common.di.CoreModule
import com.demo.tictactoe.core.common.network.BluetoothApi
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
}