package org.kimplify.kurrency

import kotlin.js.ExperimentalWasmJsInterop

/**
 * Gets the browser's default locale from navigator.language
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("function() { return navigator.language || 'en-US'; }")
private external fun getBrowserLocale(): String

/**
 * Web (JS/WasmJs) implementation of KurrencyLocale using BCP 47 language tags.
 */
actual class KurrencyLocale internal constructor(actual val languageTag: String) {

    actual companion object {
        actual fun fromLanguageTag(languageTag: String): Result<KurrencyLocale> {
            return try {
                // Basic validation for BCP 47 format
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
            // Get browser's default locale
            val browserLocale = getBrowserLocale()
            return KurrencyLocale(browserLocale)
        }

        // Predefined locales
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
        private fun isValidLanguageTag(tag: String): Boolean {
            // Simple regex for BCP 47: language code (2-3 letters) optionally followed by subtags
            val bcp47Pattern = Regex(
                "^[a-z]{2,3}(-[A-Z][a-z]{3})?(-[A-Z]{2})?(-[0-9A-Za-z]+)*$",
                RegexOption.IGNORE_CASE
            )
            return bcp47Pattern.matches(tag)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KurrencyLocale) return false
        return languageTag == other.languageTag
    }

    override fun hashCode(): Int = languageTag.hashCode()

    override fun toString(): String = "KurrencyLocale($languageTag)"
}
