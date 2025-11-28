package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.kimplify.kurrency.KurrencyLocale
import org.kimplify.kurrency.SystemFormatting

/**
 * JVM implementation of rememberSystemFormatting.
 * Returns current system formatting without real-time observation.
 */
@Composable
actual fun rememberSystemFormatting(locale: KurrencyLocale): SystemFormattingSnapshot {
    return remember(locale) {
        SystemFormattingSnapshot(
            decimalSeparator = SystemFormatting(locale).decimalSeparator,
            groupingSeparator = SystemFormatting(locale).groupingSeparator
        )
    }
}
