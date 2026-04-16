package com.demo.tictactoe.ui.gamehost

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.tictactoe.common.BlePermissionHelper

@SuppressLint("ContextCastToActivity")
@Composable
fun HostScreen(
    onStartWaiting: () -> Unit
) {
    val activity = LocalContext.current as Activity

    val discoverableLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onStartWaiting() // ✅ Navigate ONLY
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->

        val allGranted = result.values.all { it }
        if (!allGranted) return@rememberLauncherForActivityResult

        if (!BlePermissionHelper.isGpsEnabled(activity)) {
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return@rememberLauncherForActivityResult
        }

        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }

        discoverableLauncher.launch(intent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Host Game", color = Color.White, fontSize = 28.sp)

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    permissionLauncher.launch(
                        BlePermissionHelper.requiredPermissions()
                    )
                }
            ) {
                Text("Start Hosting")
            }
        }
    }
}