package org.kimplify.kurrency


/**
 * Gets the browser's default locale from navigator.language
 */
expect fun getBrowserLocale(): String

/**
 * Gets the decimal separator for a given locale using Intl.NumberFormat
 */
expect fun getDecimalSeparatorForLocale(locale: String): String

/**
 * Gets the grouping separator for a given locale using Intl.NumberFormat
 */
expect fun getGroupingSeparatorForLocale(locale: String): String

/**
 * Web (JS/WasmJs) implementation of KurrencyLocale using BCP 47 language tags.
 */
actual class KurrencyLocale internal constructor(actual val languageTag: String) {

    actual val decimalSeparator: Char
        get() = getDecimalSeparatorForLocale(languageTag).firstOrNull() ?: '.'

    actual val groupingSeparator: Char
        get() = getGroupingSeparatorForLocale(languageTag).firstOrNull() ?: ','

    actual val usesCommaAsDecimalSeparator: Boolean
        get() = decimalSeparator == ','

    actual companion object {
        actual fun fromLanguageTag(languageTag: String): Result<KurrencyLocale> {
            return try {
                if (languageTag.isBlank()) {
                    Result.failure(IllegalArgumentException("Language tag cannot be blank"))
                } else if (!isValidLanguageTag(languageTag)) {
                    Result.failure(IllegalArgumentException("Invalid language tag format: $languageTag"))
                } else {
                    Result.success(KurrencyLocale(languageTag))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        actual fun systemLocale(): KurrencyLocale {
            val browserLocale = getBrowserLocale()
            return KurrencyLocale(browserLocale)
        }

        actual val US: KurrencyLocale = KurrencyLocale("en-US")
        actual val UK: KurrencyLocale = KurrencyLocale("en-GB")
        actual val CANADA: KurrencyLocale = KurrencyLocale("en-CA")
        actual val CANADA_FRENCH: KurrencyLocale = KurrencyLocale("fr-CA")
        actual val GERMANY: KurrencyLocale = KurrencyLocale("de-DE")
        actual val FRANCE: KurrencyLocale = KurrencyLocale("fr-FR")
        actual val ITALY: KurrencyLocale = KurrencyLocale("it-IT")
        actual val SPAIN: KurrencyLocale = KurrencyLocale("es-ES")
        actual val JAPAN: KurrencyLocale = KurrencyLocale("ja-JP")
        actual val CHINA: KurrencyLocale = KurrencyLocale("zh-CN")
        actual val KOREA: KurrencyLocale = KurrencyLocale("ko-KR")
        actual val BRAZIL: KurrencyLocale = KurrencyLocale("pt-BR")
        actual val RUSSIA: KurrencyLocale = KurrencyLocale("ru-RU")
        actual val SAUDI_ARABIA: KurrencyLocale = KurrencyLocale("ar-SA")
        actual val INDIA: KurrencyLocale = KurrencyLocale("hi-IN")

        /**
         * Basic validation for BCP 47 language tags.
         * Format: language[-script][-region][-variant]
         */
        private fun isValidLanguageTag(tag: String): Boolean =
            BCP47_LANGUAGE_TAG_REGEX.matches(tag)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KurrencyLocale) return false
        return languageTag == other.languageTag
    }

    override fun hashCode(): Int = languageTag.hashCode()

    override fun toString(): String = "KurrencyLocale($languageTag)"
}
