
package com.example.lab5.extensions
import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.decodeToImageBitmap


fun ByteArray.toImageBitmap(): ImageBitmap {
    return decodeToImageBitmap()
}
