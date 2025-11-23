package com.chilinoodles.kurrency

import java.util.Locale

/**
 * Android implementation of KurrencyLocale using java.util.Locale.
 */
actual class KurrencyLocale internal constructor(internal val locale: Locale) {
    actual val languageTag: String
        get() = locale.toLanguageTag()

    actual companion object {
        actual fun fromLanguageTag(languageTag: String): Result<KurrencyLocale> {
            return try {
                // Validate format before creating locale
                if (languageTag.isBlank()) {
                    return Result.failure(IllegalArgumentException("Language tag cannot be blank"))
                }

                // Basic BCP 47 validation
                val bcp47Pattern = Regex(
                    "^[a-z]{2,3}(-[A-Z][a-z]{3})?(-[A-Z]{2})?(-[0-9A-Za-z]+)*$",
                    RegexOption.IGNORE_CASE
                )

                if (!bcp47Pattern.matches(languageTag)) {
                    return Result.failure(IllegalArgumentException("Invalid language tag format: $languageTag"))
                }

                val locale = Locale.forLanguageTag(languageTag)
                Result.success(KurrencyLocale(locale))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        actual fun systemLocale(): KurrencyLocale {
            return KurrencyLocale(Locale.getDefault())
        }

        // Predefined locales
        actual val US: KurrencyLocale = KurrencyLocale(Locale.US)
        actual val UK: KurrencyLocale = KurrencyLocale(Locale.UK)
        actual val CANADA: KurrencyLocale = KurrencyLocale(Locale.CANADA)
        actual val CANADA_FRENCH: KurrencyLocale = KurrencyLocale(Locale.CANADA_FRENCH)
        actual val GERMANY: KurrencyLocale = KurrencyLocale(Locale.GERMANY)
        actual val FRANCE: KurrencyLocale = KurrencyLocale(Locale.FRANCE)
        actual val ITALY: KurrencyLocale = KurrencyLocale(Locale.ITALY)
        actual val SPAIN: KurrencyLocale = KurrencyLocale(Locale.forLanguageTag("es-ES"))
        actual val JAPAN: KurrencyLocale = KurrencyLocale(Locale.JAPAN)
        actual val CHINA: KurrencyLocale = KurrencyLocale(Locale.CHINA)
        actual val KOREA: KurrencyLocale = KurrencyLocale(Locale.KOREA)
        actual val BRAZIL: KurrencyLocale = KurrencyLocale(Locale.forLanguageTag("pt-BR"))
        actual val RUSSIA: KurrencyLocale = KurrencyLocale(Locale.forLanguageTag("ru-RU"))
        actual val SAUDI_ARABIA: KurrencyLocale = KurrencyLocale(Locale.forLanguageTag("ar-SA"))
        actual val INDIA: KurrencyLocale = KurrencyLocale(Locale.forLanguageTag("hi-IN"))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KurrencyLocale) return false
        return locale == other.locale
    }

    override fun hashCode(): Int = locale.hashCode()

    override fun toString(): String = "KurrencyLocale($languageTag)"
}
