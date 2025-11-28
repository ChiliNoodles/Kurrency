package org.kimplify.kurrency.compose

import androidx.compose.ui.text.intl.Locale
import org.kimplify.kurrency.KurrencyLocale

/**
 * JS/WasmJS implementation: Converts Compose Locale to language tag.
 *
 * The JS/WWasmJS implementation of KurrencyLocale stores the language tag as a String,
 * so we extract and use the Compose locale's language tag directly.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.toLanguageTag())
}