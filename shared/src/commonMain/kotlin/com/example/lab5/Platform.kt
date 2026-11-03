package com.example.lab5

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform