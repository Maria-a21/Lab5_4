package com.example.lab5.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.lab5.model.Student

@Composable
fun StudentLazyList(
    students: List<Student>,
    onLikeClick: (String) -> Unit,
    onCameraClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = students,
            key = { it.id }
        ) { student ->
            StudentCard(
                student = student,
                onLikeClick = { onLikeClick(student.id) },
                onCameraClick = { onCameraClick(student.id) }
            )
        }
    }
}