package com.example.lab5.utils

import kotlin.random.Random

fun generateUuid(): String {
    return List(32) {
        "0123456789abcdef"[Random.nextInt(16)]
    }.joinToString("")
}