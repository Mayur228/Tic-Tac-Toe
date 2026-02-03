package com.demo.tictactoe.di

import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.framework.BluetoothApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindBluetoothApiProvider(impl: BluetoothApiImpl): BluetoothApi
}
