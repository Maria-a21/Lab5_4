
package com.example.lab5.model

data class Student(
    val id: String,
    val name: String,
    val group: String = "",
    val imageBytes: ByteArray? = null,
    val isLiked: Boolean = false
)
