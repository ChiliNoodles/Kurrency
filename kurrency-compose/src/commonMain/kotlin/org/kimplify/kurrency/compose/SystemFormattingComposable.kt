package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import org.kimplify.kurrency.KurrencyLocale

/**
 * Composable that provides current system formatting preferences.
 * Will recompose when system formatting settings change.
 *
 * On iOS, this observes NSCurrentLocaleDidChangeNotification to detect changes.
 * On other platforms, this observes the compose locale for recomposition changes.
 *
 * @return Snapshot of current system formatting settings
 */
@Composable
expect fun rememberSystemFormatting(locale: KurrencyLocale = KurrencyLocale.current()): SystemFormattingSnapshot

/**
 * Snapshot of system formatting preferences at a point in time.
 *
 * @property decimalSeparator The decimal separator (e.g., '.' or ',')
 * @property groupingSeparator The grouping separator (e.g., ',' or '.')
 */
data class SystemFormattingSnapshot(
    val decimalSeparator: String,
    val groupingSeparator: String
)
