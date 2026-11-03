package com.example.lab5.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.Executor

@Composable
actual fun CameraView(
    onPhotoCaptured: (ByteArray) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember(context) {
        ContextCompat.getMainExecutor(context)
    }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var previewView by remember {
        mutableStateOf<PreviewView?>(null)
    }
    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            errorMessage = "Надай дозвіл на використання камери."
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Камера")

        if (hasPermission) {
            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                        previewView = this
                    }
                },
                update = { view ->
                    if (previewView !== view) {
                        previewView = view
                    }
                }
            )

            LaunchedEffect(previewView, hasPermission) {
                val view = previewView ?: return@LaunchedEffect

                try {
                    val providerFuture =
                        ProcessCameraProvider.getInstance(context)

                    providerFuture.addListener({
                        try {
                            val provider = providerFuture.get()

                            val preview = Preview.Builder().build().apply {
                                surfaceProvider = view.surfaceProvider
                            }

                            val capture = ImageCapture.Builder()
                                .setCaptureMode(
                                    ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                                )
                                .build()

                            provider.unbindAll()

                            val selector = when {
                                provider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) -> CameraSelector.DEFAULT_FRONT_CAMERA
                                provider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) -> CameraSelector.DEFAULT_BACK_CAMERA
                                else -> null
                            }
                            if (selector == null) {
                                imageCapture = null
                                errorMessage =
                                    "Камеру не знайдено. Перевір налаштування емулятора."
                                return@addListener
                            }

                            provider.bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                capture
                            )

                            imageCapture = capture
                            errorMessage = null
                        } catch (e: Exception) {
                            imageCapture = null
                            errorMessage =
                                "Не вдалося запустити камеру: ${e.message}"
                        }
                    }, executor)
                } catch (e: Exception) {
                    errorMessage =
                        "Помилка доступу до камери: ${e.message}"
                }
            }

            errorMessage?.let { message ->
                Text(message)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Скасувати")
                }

                Button(
                    onClick = {
                        val capture = imageCapture

                        if (capture == null) {
                            errorMessage = "Камера ще не готова."
                        } else {
                            takePhoto(
                                context = context,
                                imageCapture = capture,
                                executor = executor,
                                onPhotoCaptured = onPhotoCaptured,
                                onError = { errorMessage = it }
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = imageCapture != null
                ) {
                    Text("Зробити фото")
                }
            }
        } else {
            Text(
                errorMessage
                    ?: "Очікується дозвіл на використання камери."
            )

            Button(onClick = onClose) {
                Text("Закрити")
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                ProcessCameraProvider.getInstance(context).get().unbindAll()
            } catch (_: Exception) {}
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onPhotoCaptured: (ByteArray) -> Unit,
    onError: (String) -> Unit
) {
    val photoFile = File.createTempFile(
        "student_photo_",
        ".jpg",
        context.cacheDir
    )

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(photoFile)
        .build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(
                outputFileResults: ImageCapture.OutputFileResults
            ) {
                try {
                    val bytes = photoFile.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(
                        bytes, 0, bytes.size
                    ) ?: throw IllegalStateException(
                        "Не вдалося прочитати фотографію."
                    )

                    try {
                        val squareBitmap = cropCenterSquare(bitmap)

                        try {
                            val output = ByteArrayOutputStream()
                            squareBitmap.compress(
                                Bitmap.CompressFormat.JPEG,
                                90,
                                output
                            )
                            onPhotoCaptured(output.toByteArray())
                        } finally {
                            if (squareBitmap !== bitmap) {
                                squareBitmap.recycle()
                            }
                        }
                    } finally {
                        bitmap.recycle()
                    }
                } catch (e: Exception) {
                    onError("Помилка обробки фото: ${e.message}")
                } finally {
                    photoFile.delete()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onError("Не вдалося зробити фото: ${exception.message}")
                photoFile.delete()
            }
        }
    )
}

private fun cropCenterSquare(bitmap: Bitmap): Bitmap {
    val side = minOf(bitmap.width, bitmap.height)
    val left = (bitmap.width - side) / 2
    val top = (bitmap.height - side) / 2

    return Bitmap.createBitmap(
        bitmap,
        left,
        top,
        side,
        side
    )
}