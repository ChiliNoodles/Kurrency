package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * Creates a [KurrencyLocale] from a Jetpack Compose Multiplatform [Locale].
 *
 * Uses platform-specific direct conversion for better performance and accuracy.
 *
 * @param composeLocale The Compose locale to convert
 * @return KurrencyLocale instance
 */
expect fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale

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
        fromComposeLocale(composeLocale)
    }
}

/**
 * Extension function to convert a Compose Locale to a KurrencyLocale.
 *
 * Uses platform-specific direct conversion for zero overhead.
 *
 * Usage:
 * ```
 * val composeLocale = Locale.current
 * val kurrencyLocale = composeLocale.toKurrencyLocale()
 * val decimalSep = kurrencyLocale.decimalSeparator
 * ```
 */
fun Locale.toKurrencyLocale(): KurrencyLocale {
    return KurrencyLocale.fromComposeLocale(this)
}
