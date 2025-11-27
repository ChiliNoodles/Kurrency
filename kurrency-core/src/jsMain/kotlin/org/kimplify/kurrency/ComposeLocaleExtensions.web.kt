package org.kimplify.kurrency

import androidx.compose.ui.text.intl.Locale

/**
 * Web implementation: Uses language tag directly as it's the native format
 * for web Intl APIs.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.toLanguageTag())
}
