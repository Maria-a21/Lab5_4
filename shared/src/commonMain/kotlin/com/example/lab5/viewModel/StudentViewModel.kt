
package com.example.lab5.viewModel

import androidx.lifecycle.ViewModel
import com.example.lab5.model.Student
import com.example.lab5.state.StudentUiState
import com.example.lab5.utils.generateUuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> = _uiState.asStateFlow()

    fun toggleLike(studentId: String) {
        _uiState.update { state ->
            state.copy(
                students = state.students.map { student ->
                    if (student.id == studentId) {
                        student.copy(isLiked = !student.isLiked)
                    } else {
                        student
                    }
                }
            )
        }
    }

    fun startCameraForStudent(studentId: String) {
        _uiState.update {
            it.copy(
                showCamera = true,
                selectedStudentId = studentId
            )
        }
    }

    fun dismissCamera() {
        _uiState.update {
            it.copy(
                showCamera = false,
                selectedStudentId = null
            )
        }
    }

    fun updateStudentAvatar(studentId: String, imageBytes: ByteArray) {
        _uiState.update { state ->
            state.copy(
                students = state.students.map { student ->
                    if (student.id == studentId) {
                        student.copy(imageBytes = imageBytes)
                    } else {
                        student
                    }
                },
                showCamera = false,
                selectedStudentId = null
            )
        }
    }

    fun showAddStudentDialog() {
        _uiState.update {
            it.copy(
                showDialog = true,
                errorMessage = null
            )
        }
    }

    fun dismissAddStudentDialog() {
        _uiState.update {
            it.copy(
                showDialog = false,
                errorMessage = null
            )
        }
    }

    fun addNewStudent(name: String, group: String) {
        if (name.isBlank() || group.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Введіть ім'я та групу студента")
            }
            return
        }

        val newStudent = Student(
            id = generateUuid(),
            name = name.trim(),
            group = group.trim()
        )

        _uiState.update { state ->
            state.copy(
                students = state.students + newStudent,
                showDialog = false,
                errorMessage = null
            )
        }
    }
}
