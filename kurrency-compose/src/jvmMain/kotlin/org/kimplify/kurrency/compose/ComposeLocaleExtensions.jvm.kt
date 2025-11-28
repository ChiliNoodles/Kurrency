package org.kimplify.kurrency.compose

import androidx.compose.ui.text.intl.Locale
import org.kimplify.kurrency.KurrencyLocale

/**
 * JVM implementation: Uses platformLocale for direct native locale access.
 * Zero overhead - no string conversion or parsing.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.platformLocale)
}
