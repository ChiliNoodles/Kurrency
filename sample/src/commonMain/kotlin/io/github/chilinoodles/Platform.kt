package io.github.chilinoodles

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform