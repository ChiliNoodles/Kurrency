package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.kimplify.kurrency.KurrencyLocale
import org.kimplify.kurrency.SystemFormatting
import platform.Foundation.NSCurrentLocaleDidChangeNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue

/**
 * iOS implementation of rememberSystemFormatting.
 * Observes NSCurrentLocaleDidChangeNotification to detect when user changes formatting settings.
 */
@Composable
actual fun rememberSystemFormatting(locale: KurrencyLocale): SystemFormattingSnapshot {
    var localeVersion by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = NSCurrentLocaleDidChangeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            localeVersion++
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    return remember(localeVersion) {
        SystemFormattingSnapshot(
            decimalSeparator = SystemFormatting.decimalSeparator,
            groupingSeparator = SystemFormatting.groupingSeparator
        )
    }
}
