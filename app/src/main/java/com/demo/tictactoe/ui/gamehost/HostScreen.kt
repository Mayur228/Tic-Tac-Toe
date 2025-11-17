package com.demo.tictactoe.ui.gamehost

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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

@SuppressLint("MissingPermission", "ContextCastToActivity")
@Composable
fun HostJoinScreen(
    viewModel: GameViewModel,
    onHost: () -> Unit,
    onJoin: () -> Unit
) {
    val activity = LocalContext.current as Activity
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->

        // If ANY permission is denied → stop
        val allGranted = result.values.all { it }
        if (!allGranted) {
            return@rememberLauncherForActivityResult
        }

        // GPS check after permission
        if (!BlePermissionHelper.isGpsEnabled(activity)) {
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return@rememberLauncherForActivityResult
        }

        // Execute the pending action (Host or Join)
        pendingAction?.invoke()
        pendingAction = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            "Tic Tac Toe",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(40.dp))

        // ---------------- HOST BUTTON ----------------
        GlowButton(
            text = "Host Game",
            color = Color(0xFF4C9EFF)
        ) {
            pendingAction = {
                // Make discoverable
                val discoverIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                }
                activity.startActivity(discoverIntent)

                viewModel.hostGame("Tic Tac Toe Host")
                onHost()
            }

            permissionLauncher.launch(BlePermissionHelper.requiredPermissions())
        }

        Spacer(Modifier.height(20.dp))

        // ---------------- JOIN BUTTON ----------------
        GlowButton(
            text = "Join Game",
            color = Color(0xFFFF61C6)
        ) {
            pendingAction = {
                viewModel.startScan()
                onJoin()
            }

            permissionLauncher.launch(BlePermissionHelper.requiredPermissions())
        }
    }
}

@Composable
fun GlowButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(230.dp)
            .height(55.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
