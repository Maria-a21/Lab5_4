
package com.example.lab5.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.lab5.extensions.toImageBitmap

@Composable
fun StudentAvatar(
    imageBytes: ByteArray?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (imageBytes != null) {
                Image(
                    bitmap = imageBytes.toImageBitmap(),
                    contentDescription = "Фото студента",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "👤"
                )
            }
        }
    }
}
