package io.github.chilinoodles

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chilinoodles.kurrency.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kurrency",
    ) {
        App()
    }
}