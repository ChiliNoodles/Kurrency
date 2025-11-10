package io.github.chilinoodles

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
<<<<<<< HEAD
import com.chilinoodles.kurrency.sample.App
=======
>>>>>>> origin/main

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kurrency",
    ) {
        App()
    }
}