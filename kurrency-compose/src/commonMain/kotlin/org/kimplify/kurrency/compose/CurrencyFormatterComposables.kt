package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import org.kimplify.kurrency.CurrencyFormat
import org.kimplify.kurrency.CurrencyFormatter
import org.kimplify.kurrency.KurrencyLocale
import org.kimplify.kurrency.current

/**
 * CompositionLocal for providing a default [CurrencyFormat] throughout the composition hierarchy.
 *
 * By default, uses the system locale. Override this to provide a custom formatter for the entire
 * app or a subtree of the composition.
 *
 * Example usage:
 * ```
 * CompositionLocalProvider(LocalCurrencyFormatter provides customFormatter) {
 *     // All composables in this tree will use customFormatter by default
 *     MyApp()
 * }
 * ```
 */
val LocalCurrencyFormatter = staticCompositionLocalOf<CurrencyFormat> {
    CurrencyFormatter()
}

/**
 * Remembers a [CurrencyFormat] instance with the specified locale.
 *
 * The formatter will be recreated whenever the locale changes, ensuring that formatting
 * automatically updates when the locale is changed.
 *
 * @param locale The locale to use for formatting. When this value changes, a new formatter
 *               will be created and composables will recompose.
 * @return A memoized formatter instance
 *
 * Example usage:
 * ```
 * @Composable
 * fun PriceDisplay(amount: String, currencyCode: String) {
 *     var selectedLocale by remember { mutableStateOf(KurrencyLocale.US) }
 *     val formatter = rememberCurrencyFormatter(locale = selectedLocale)
 *
 *     val formattedPrice = remember(amount, currencyCode) {
 *         formatter.formatCurrencyStyle(amount, currencyCode)
 *     }
 *
 *     Text(formattedPrice)
 * }
 * ```
 */
@Composable
fun rememberCurrencyFormatter(locale: KurrencyLocale = KurrencyLocale.current()): CurrencyFormat {
    return remember(locale) {
        CurrencyFormatter(locale)
    }
}

/**
 * Remembers a [CurrencyFormat] instance with the system's default locale.
 *
 * This is useful when you want to explicitly use the system locale and recreate
 * the formatter in response to system locale changes.
 *
 * @return A memoized formatter instance using the system locale
 *
 * Example usage:
 * ```
 * @Composable
 * fun PriceDisplay(amount: String, currencyCode: String) {
 *     val formatter = rememberSystemCurrencyFormatter()
 *     val formattedPrice = remember(amount, currencyCode) {
 *         formatter.formatCurrencyStyle(amount, currencyCode)
 *     }
 *     Text(formattedPrice)
 * }
 * ```
 */
@Composable
fun rememberSystemCurrencyFormatter(): CurrencyFormat {
    val systemLocale = KurrencyLocale.systemLocale()
    return remember(systemLocale) {
        CurrencyFormatter(systemLocale)
    }
}

/**
 * Provides a [CurrencyFormat] to the composition hierarchy using [LocalCurrencyFormatter].
 *
 * All child composables can access this formatter using `LocalCurrencyFormatter.current`.
 *
 * @param locale The locale to use for the provided formatter
 * @param content The composable content that will have access to this formatter
 *
 * Example usage:
 * ```
 * @Composable
 * fun App() {
 *     var appLocale by remember { mutableStateOf(KurrencyLocale.US) }
 *
 *     ProvideCurrencyFormatter(locale = appLocale) {
 *         // All composables here can use LocalCurrencyFormatter.current
 *         Screen1()
 *         Screen2()
 *     }
 * }
 *
 * @Composable
 * fun Screen1() {
 *     val formatter = LocalCurrencyFormatter.current
 *     val price = formatter.formatCurrencyStyle("100.50", "USD")
 *     Text(price)
 * }
 * ```
 */
@Composable
fun ProvideCurrencyFormatter(
    locale: KurrencyLocale,
    content: @Composable () -> Unit
) {
    val formatter = rememberCurrencyFormatter(locale)
    CompositionLocalProvider(LocalCurrencyFormatter provides formatter) {
        content()
    }
}

/**
 * Provides a [CurrencyFormat] using the system locale to the composition hierarchy.
 *
 * This is a convenience function equivalent to `ProvideCurrencyFormatter(KurrencyLocale.systemLocale())`.
 *
 * @param content The composable content that will have access to this formatter
 *
 * Example usage:
 * ```
 * @Composable
 * fun App() {
 *     ProvideSystemCurrencyFormatter {
 *         MyAppContent()
 *     }
 * }
 * ```
 */
@Composable
fun ProvideSystemCurrencyFormatter(
    content: @Composable () -> Unit
) {
    val formatter = rememberSystemCurrencyFormatter()
    CompositionLocalProvider(LocalCurrencyFormatter provides formatter) {
        content()
    }
}
