package org.kimplify.kurrency


class SystemFormatting(val locale: KurrencyLocale) : ISystemFormatting {
    override val decimalSeparator: String
        get() = getDecimalSeparatorForLocale(locale.languageTag).firstOrNull()?.toString() ?: "."

    override val groupingSeparator: String
        get() = getGroupingSeparatorForLocale(locale.languageTag).firstOrNull()?.toString() ?: ""
}