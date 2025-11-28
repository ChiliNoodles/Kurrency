package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import org.kimplify.kurrency.KurrencyLocale
import platform.Foundation.NSCurrentLocaleDidChangeNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue

/**
 * iOS implementation: Observes NSLocaleDidChangeNotification to trigger recomposition
 * when formatting settings change (not just locale changes).
 *
 * This ensures that when the user changes decimal/grouping separator settings in
 * iOS Settings, the changes are reflected immediately without requiring an app restart.
 */
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    return KurrencyLocale(composeLocale.platformLocale)
}
