
package com.example.lab5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5.components.CameraView
import com.example.lab5.components.StudentDialog
import com.example.lab5.components.StudentLazyList
import com.example.lab5.viewModel.StudentViewModel

@Composable
fun App() {
    val viewModel = remember { StudentViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.students.isEmpty()) {
            viewModel.addNewStudent(
                "Артимович Марія",
                "ITSEC"
            )
        }
    }

    MaterialTheme {
        if (uiState.showCamera &&
            uiState.selectedStudentId != null
        ) {
            CameraView(
                onPhotoCaptured = { photoBytes ->
                    viewModel.updateStudentAvatar(
                        uiState.selectedStudentId!!,
                        photoBytes
                    )
                },
                onClose = {
                    viewModel.dismissCamera()
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Картки студентів (CameraX + Coroutines)",
                    style = MaterialTheme.typography.titleLarge
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    StudentLazyList(
                        students = uiState.students,
                        onLikeClick = { studentId ->
                            viewModel.toggleLike(studentId)
                        },
                        onCameraClick = { studentId ->
                            viewModel.startCameraForStudent(
                                studentId
                            )
                        }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.showAddStudentDialog()
                        }
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }

        if (uiState.showDialog) {
            StudentDialog(
                onDismiss = {
                    viewModel.dismissAddStudentDialog()
                },
                onAdd = { name, group ->
                    viewModel.addNewStudent(name, group)
                }
            )
        }
    }
}
