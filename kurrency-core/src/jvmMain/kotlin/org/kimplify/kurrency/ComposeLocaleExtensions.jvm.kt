package org.kimplify.kurrency

import androidx.compose.ui.text.intl.Locale

/**
 * JVM implementation: Uses platformLocale for direct native locale access.
 * Zero overhead - no string conversion or parsing.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.platformLocale)
}
