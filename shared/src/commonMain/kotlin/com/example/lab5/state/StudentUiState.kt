
package com.example.lab5.state

import com.example.lab5.model.Student

data class StudentUiState(
    val students: List<Student> = emptyList(),
    val studentName: String = "",
    val showDialog: Boolean = false,
    val showCamera: Boolean = false,
    val selectedStudentId: String? = null,
    val errorMessage: String? = null
)
