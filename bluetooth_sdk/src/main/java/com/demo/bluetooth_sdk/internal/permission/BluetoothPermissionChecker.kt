package com.demo.bluetooth_sdk.internal.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.demo.bluetooth_sdk.api.BluetoothException

internal class BluetoothPermissionChecker(private val context: Context) {

    companion object {
        // Static helpers for internal classes that used the old static call style
        fun hasScanPermission(context: Context): Boolean {
            return BluetoothPermissionChecker(context).hasScanPermission()
        }

        fun hasConnectPermission(context: Context): Boolean {
            return BluetoothPermissionChecker(context).hasConnectPermission()
        }

        fun hasAdvertisePermission(context: Context): Boolean {
            return BluetoothPermissionChecker(context).hasAdvertisePermission()
        }
    }

    // ------------------------------
    // Instance check methods
    // ------------------------------
    fun checkScanPermission() {
        if (!hasScanPermission()) throw BluetoothException.PermissionDenied(scanPermissions())
    }

    fun checkConnectPermission() {
        if (!hasConnectPermission()) throw BluetoothException.PermissionDenied(connectPermissions())
    }

    fun checkAdvertisePermission() {
        if (!hasAdvertisePermission()) throw BluetoothException.PermissionDenied(advertisePermissions())
    }

    // ------------------------------
    // Internal helpers
    // ------------------------------
    private fun hasScanPermission(): Boolean = scanPermissions().all { hasPermission(it) }
    private fun hasConnectPermission(): Boolean = connectPermissions().all { hasPermission(it) }
    private fun hasAdvertisePermission(): Boolean = advertisePermissions().all { hasPermission(it) }
    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun scanPermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        listOf(Manifest.permission.BLUETOOTH_SCAN)
    else
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun connectPermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        listOf(Manifest.permission.BLUETOOTH_CONNECT)
    else
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )

    private fun advertisePermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        listOf(Manifest.permission.BLUETOOTH_ADVERTISE)
    else
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
}
