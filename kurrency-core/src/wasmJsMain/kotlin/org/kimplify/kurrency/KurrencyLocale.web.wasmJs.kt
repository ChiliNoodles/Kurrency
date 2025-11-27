package org.kimplify.kurrency

/**
 * Gets the browser's default locale from navigator.language
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("function() { return navigator.language || 'en-US'; }")
private external fun getBrowserLocaleInternal(): String

/**
 * Gets the decimal separator for a given locale using Intl.NumberFormat
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("function(locale) { const formatted = new Intl.NumberFormat(locale).format(1.1); return formatted.replace(/[0-9]/g, '')[0] || '.'; }")
private external fun getDecimalSeparatorForLocaleInternal(locale: String): String

/**
 * Gets the grouping separator for a given locale using Intl.NumberFormat
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("function(locale) { const formatted = new Intl.NumberFormat(locale).format(1111); return formatted.replace(/[0-9]/g, '')[0] || ','; }")
private external fun getGroupingSeparatorForLocaleInternal(locale: String): String


actual fun getBrowserLocale(): String {
    return getBrowserLocaleInternal()
}

actual fun getDecimalSeparatorForLocale(locale: String): String {
    return getDecimalSeparatorForLocaleInternal(locale)
}

actual fun getGroupingSeparatorForLocale(locale: String): String {
    return getGroupingSeparatorForLocaleInternal(locale)
}