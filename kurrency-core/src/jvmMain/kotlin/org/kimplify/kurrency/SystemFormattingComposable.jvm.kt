package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * JVM implementation of rememberSystemFormatting.
 * Returns current system formatting without real-time observation.
 */
@Composable
actual fun rememberSystemFormatting(locale: KurrencyLocale): SystemFormattingSnapshot {
    return remember(locale) {
        SystemFormattingSnapshot(
            decimalSeparator = SystemFormatting(locale.locale).decimalSeparator,
            groupingSeparator = SystemFormatting(locale.locale).groupingSeparator
        )
    }
}
