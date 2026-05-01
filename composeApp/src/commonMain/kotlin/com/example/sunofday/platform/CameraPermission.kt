package com.example.sunofday.platform

import androidx.compose.runtime.Composable

@Composable
expect fun RequestCameraPermission()

@Composable
expect fun rememberHasCameraPermission(): Boolean
