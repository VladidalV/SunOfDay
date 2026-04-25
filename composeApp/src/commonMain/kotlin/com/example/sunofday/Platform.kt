package com.example.sunofday

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform