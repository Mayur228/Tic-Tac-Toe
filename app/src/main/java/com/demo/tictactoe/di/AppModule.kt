package com.demo.tictactoe.di

import android.content.Context
import com.demo.bluetooth_sdk.BluetoothClassicSdk
import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.framework.BluetoothApiImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object  {
//        @Provides
//        @Singleton
//        fun provideBluetoothApi(): ClassicBluetoothManager {
//            return BluetoothClassicSdk.Manager.bluetoothSdkManager
//        }
    }

    @Binds
    abstract fun bindBluetoothApiProvider(impl: BluetoothApiImpl): BluetoothApi
}
