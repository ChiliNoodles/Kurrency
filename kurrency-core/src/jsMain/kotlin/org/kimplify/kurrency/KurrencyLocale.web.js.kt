package org.kimplify.kurrency

external object Intl {
    class NumberFormat(locales: String? = definedExternally, options: dynamic = definedExternally) {
        fun format(number: Number): String
    }
}

/**
 * Gets the browser's default locale from navigator.language
 */
actual fun getBrowserLocale(): String {
    return js("navigator.language || 'en-US'") as String
}

/**
 * Gets the decimal separator for a given locale using Intl.NumberFormat
 */
actual fun getDecimalSeparatorForLocale(locale: String): String {
    val formatter = Intl.NumberFormat(locale)
    val formatted = formatter.format(1.1)
    return formatted.replace(Regex("[0-9]"), "").firstOrNull()?.toString() ?: "."
}

/**
 * Gets the grouping separator for a given locale using Intl.NumberFormat
 */
actual fun getGroupingSeparatorForLocale(locale: String): String {
    val formatter = Intl.NumberFormat(locale)
    val formatted = formatter.format(1111)
    return formatted.replace(Regex("[0-9]"), "").firstOrNull()?.toString() ?: ","
}