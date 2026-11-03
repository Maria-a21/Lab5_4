import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5.components.StudentAvatar
import com.example.lab5.model.Student

@Composable
fun StudentListScreen(
    students: List<Student>,
    onLikeClick: (String) -> Unit,
    onAvatarClick: (String) -> Unit,
    onAddStudentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Список студентів",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.size(12.dp))

        Button(
            onClick = onAddStudentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати студента")
        }

        Spacer(modifier = Modifier.size(16.dp))

        if (students.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Список студентів поки порожній",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Натисни «Додати студента», щоб почати"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = students,
                    key = { student -> student.id }
                ) { student ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StudentAvatar(
                                imageBytes = student.imageBytes,
                                onClick = {
                                    onAvatarClick(student.id)
                                }
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Натисни на фото, щоб відкрити камеру",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(
                                onClick = {
                                    onLikeClick(student.id)
                                }
                            ) {
                                Text(
                                    text = if (student.isLiked) "♥" else "♡",
                                    style = MaterialTheme.typography.headlineSmall,
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
            }
        }
    }
}
