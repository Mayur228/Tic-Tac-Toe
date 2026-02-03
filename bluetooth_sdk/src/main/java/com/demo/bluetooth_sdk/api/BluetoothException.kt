package com.demo.bluetooth_sdk.api

public sealed class BluetoothException(message: String) : Exception(message) {

    public class PermissionDenied(
        public val requiredPermissions: List<String>
    ) : BluetoothException(
        "Missing required Bluetooth permissions: $requiredPermissions"
    )

    public object DeviceNotFound :
        BluetoothException("Bluetooth device not found")

    public object BluetoothDisabled :
        BluetoothException("Bluetooth is disabled")

    public object ConnectionFailed :
        BluetoothException("Bluetooth connection failed")
}
