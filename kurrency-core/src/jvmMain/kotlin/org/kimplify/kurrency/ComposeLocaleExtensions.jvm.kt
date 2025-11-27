package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * JVM implementation: Uses platformLocale for direct native locale access.
 * Zero overhead - no string conversion or parsing.
 */
@Composable
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return remember(composeLocale) {
        KurrencyLocale(composeLocale.platformLocale)
    }
}
