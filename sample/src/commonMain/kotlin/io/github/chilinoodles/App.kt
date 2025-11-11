package io.github.chilinoodles

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.chilinoodles.kurrency.sample.KurrencySampleApp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        KurrencySampleApp()
    }
}