package com.demo.bluetooth_sdk.api

public sealed class BluetoothError {

    public object PermissionDenied : BluetoothError()

    public object BluetoothDisabled : BluetoothError()

    public object ConnectionFailed : BluetoothError()

    public data class Unknown(val cause: Throwable? = null) : BluetoothError()
}
