package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

/**
 * Android implementation of rememberSystemFormatting.
 * Returns current system formatting without real-time observation.
 */
@Composable
actual fun rememberSystemFormatting(locale: KurrencyLocale): SystemFormattingSnapshot {
    return remember {
        val systemFormatting = SystemFormatting(locale.locale)
        SystemFormattingSnapshot(
            decimalSeparator = systemFormatting.decimalSeparator,
            groupingSeparator = systemFormatting.groupingSeparator
        )
    }
}
