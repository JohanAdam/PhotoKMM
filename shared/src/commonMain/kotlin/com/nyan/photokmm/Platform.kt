package com.nyan.photokmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform