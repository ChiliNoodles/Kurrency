package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * WasmJS implementation: Converts Compose Locale to language tag.
 *
 * The WasmJS implementation of KurrencyLocale stores the language tag as a String,
 * so we extract and use the Compose locale's language tag directly.
 */
@Composable
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return remember(composeLocale) {
        KurrencyLocale(composeLocale.toLanguageTag())
    }
}
