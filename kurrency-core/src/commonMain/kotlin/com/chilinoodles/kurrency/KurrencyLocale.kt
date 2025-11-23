package com.chilinoodles.kurrency

/**
 * Represents a locale for currency formatting across all platforms.
 *
 * This class wraps platform-specific locale implementations and provides
 * a consistent API for locale handling in Kurrency.
 *
 * @property languageTag The BCP 47 language tag (e.g., "en-US", "de-DE", "ja-JP")
 */
expect class KurrencyLocale {
    val languageTag: String

    companion object {
        /**
         * Creates a KurrencyLocale from a BCP 47 language tag.
         *
         * @param languageTag The language tag string (e.g., "en-US", "fr-FR")
         * @return Result with KurrencyLocale on success, or failure if the tag is invalid
         */
        fun fromLanguageTag(languageTag: String): Result<KurrencyLocale>

        /**
         * Returns the system's current locale.
         */
        fun systemLocale(): KurrencyLocale

        // Common predefined locales for convenience

        /** United States English (en-US) */
        val US: KurrencyLocale

        /** United Kingdom English (en-GB) */
        val UK: KurrencyLocale

        /** Canadian English (en-CA) */
        val CANADA: KurrencyLocale

        /** Canadian French (fr-CA) */
        val CANADA_FRENCH: KurrencyLocale

        /** German (Germany) (de-DE) */
        val GERMANY: KurrencyLocale

        /** French (France) (fr-FR) */
        val FRANCE: KurrencyLocale

        /** Italian (Italy) (it-IT) */
        val ITALY: KurrencyLocale

        /** Spanish (Spain) (es-ES) */
        val SPAIN: KurrencyLocale

        /** Japanese (Japan) (ja-JP) */
        val JAPAN: KurrencyLocale

        /** Chinese Simplified (China) (zh-CN) */
        val CHINA: KurrencyLocale

        /** Korean (South Korea) (ko-KR) */
        val KOREA: KurrencyLocale

        /** Portuguese (Brazil) (pt-BR) */
        val BRAZIL: KurrencyLocale

        /** Russian (Russia) (ru-RU) */
        val RUSSIA: KurrencyLocale

        /** Arabic (Saudi Arabia) (ar-SA) */
        val SAUDI_ARABIA: KurrencyLocale

        /** Hindi (India) (hi-IN) */
        val INDIA: KurrencyLocale
    }
}
