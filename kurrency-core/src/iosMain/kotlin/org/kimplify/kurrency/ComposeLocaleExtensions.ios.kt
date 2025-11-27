package org.kimplify.kurrency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
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
@Composable
actual fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): KurrencyLocale {
    var localeChangeCounter by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = NSCurrentLocaleDidChangeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            localeChangeCounter++
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    return remember(composeLocale, localeChangeCounter) {
        KurrencyLocale(composeLocale.platformLocale)
    }
}
