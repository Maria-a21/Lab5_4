
package com.example.lab5.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5.model.Student

@Composable
fun StudentCard(
    student: Student,
    onLikeClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudentAvatar(
                imageBytes = student.imageBytes,
                onClick = onCameraClick
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = onCameraClick,
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 10.dp
                )
            ) {
                Text("Фото")
            }

            IconButton(onClick = onLikeClick) {
                Text(
                    text = if (student.isLiked) "♥" else "♡",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (student.isLiked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}
