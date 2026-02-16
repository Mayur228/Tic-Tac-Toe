package com.demo.tictactoe.di

import android.app.Application
import com.buildwithmayur.bluetooth.api.ClassicBluetoothSdk
import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.framework.BluetoothApiImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Abstract module for interface binding
@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindBluetoothApi(impl: BluetoothApiImpl): BluetoothApi
}

// Concrete module for SDK provider
@Module
@InstallIn(SingletonComponent::class)
object SdkModule {
    @Provides
    fun provideClassicBluetoothSdk(app: Application): ClassicBluetoothSdk {
        return ClassicBluetoothSdk.initialize(app.applicationContext)
    }
}
