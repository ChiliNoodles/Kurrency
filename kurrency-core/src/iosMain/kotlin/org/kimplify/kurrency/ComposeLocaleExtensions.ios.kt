package org.kimplify.kurrency

import androidx.compose.ui.text.intl.Locale
import platform.Foundation.NSLocale

/**
 * iOS implementation: Uses platformLocale for direct NSLocale access.
 * Zero overhead - no string conversion or parsing.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.platformLocale as NSLocale)
}
