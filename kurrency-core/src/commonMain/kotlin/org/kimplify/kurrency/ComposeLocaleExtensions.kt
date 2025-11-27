package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale

/**
 * Creates a [org.kimplify.kurrency.KurrencyLocale] from a Jetpack Compose Multiplatform [Locale].
 *
 * Uses platform-specific direct conversion for better performance and accuracy.
 *
 * @param composeLocale The Compose locale to convert
 * @return KurrencyLocale instance
 */
@Composable
expect fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale

/**
 * Creates a [org.kimplify.kurrency.KurrencyLocale] from the current Compose system locale.
 *
 * This is a Composable function that will recompose when the system locale changes.
 *
 * On iOS, this will also recompose when formatting settings change (e.g., decimal separator).
 *
 * @return KurrencyLocale representing the current system locale
 */
@Composable
fun KurrencyLocale.Companion.current(): KurrencyLocale {
    val composeLocale = Locale.current
    return fromComposeLocale(composeLocale)
}