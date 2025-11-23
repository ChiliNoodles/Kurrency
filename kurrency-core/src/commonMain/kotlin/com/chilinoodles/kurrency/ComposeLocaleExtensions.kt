package com.chilinoodles.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * Creates a [KurrencyLocale] from a Jetpack Compose Multiplatform [Locale].
 *
 * @param composeLocale The Compose locale to convert
 * @return Result with KurrencyLocale on success, or failure if the conversion fails
 */
fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): Result<KurrencyLocale> {
    return fromLanguageTag(composeLocale.toLanguageTag())
}

/**
 * Creates a [KurrencyLocale] from the current Compose system locale.
 *
 * This is a Composable function that will recompose when the system locale changes.
 *
 * @return KurrencyLocale representing the current system locale
 */
@Composable
fun KurrencyLocale.Companion.current(): KurrencyLocale {
    val composeLocale = Locale.current
    return remember(composeLocale) {
        fromComposeLocale(composeLocale).getOrElse {
            // Fallback to system locale if conversion fails
            systemLocale()
        }
    }
}

/**
 * Extension function to convert a Compose Locale to a KurrencyLocale.
 *
 * Usage:
 * ```
 * val composeLocale = Locale.current
 * val kurrencyLocale = composeLocale.toKurrencyLocale().getOrNull()
 * ```
 */
fun Locale.toKurrencyLocale(): Result<KurrencyLocale> {
    return KurrencyLocale.fromLanguageTag(this.toLanguageTag())
}
