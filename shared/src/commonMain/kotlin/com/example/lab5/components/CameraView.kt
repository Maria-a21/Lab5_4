package com.example.lab5.components

import androidx.compose.runtime.Composable

@Composable
expect fun CameraView(
    onPhotoCaptured: (ByteArray) -> Unit,
    onClose: () -> Unit
)