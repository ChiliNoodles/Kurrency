package org.kimplify.kurrency

import org.kimplify.cedar.logging.Cedar
import org.kimplify.kurrency.extensions.replaceCommaWithDot

expect class CurrencyFormatterImpl(kurrencyLocale: KurrencyLocale = KurrencyLocale.systemLocale()) : CurrencyFormat {
    override fun getFractionDigits(currencyCode: String): Result<Int>
    override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String>
    override fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String>
}

expect fun isValidCurrency(currencyCode: String): Boolean

object CurrencyFormatter {
    private const val DEFAULT_FRACTION_DIGITS = 2

    private val defaultFormatter: CurrencyFormat by lazy {
        Cedar.tag("Kurrency").d("Initializing default CurrencyFormatter")
        CurrencyFormatterImpl()
    }

    /**
     * Creates a new CurrencyFormat instance with the specified locale.
     *
     * @param locale The locale to use for formatting. If null, uses the system default locale.
     * @return A new CurrencyFormat instance
     */
    fun create(locale: KurrencyLocale): CurrencyFormat {
        Cedar.tag("Kurrency").d("Creating CurrencyFormatter with locale: ${locale?.languageTag ?: "system default"}")
        return CurrencyFormatterImpl(locale)
    }

    /**
     * Creates a new CurrencyFormat instance with the system's default locale.
     *
     * @return A new CurrencyFormat instance using system locale
     */
    fun createWithSystemLocale(): CurrencyFormat {
        Cedar.tag("Kurrency").d("Creating CurrencyFormatter with system locale")
        return CurrencyFormatterImpl(KurrencyLocale.systemLocale())
    }

    fun getFractionDigits(currencyCode: String): Result<Int> {
        if (!isValidCurrencyCode(currencyCode)) {
            val error = KurrencyError.InvalidCurrencyCode(currencyCode)
            Cedar.tag("Kurrency").w(error.errorMessage)
            return Result.failure(error)
        }

        Cedar.tag("Kurrency").d("Getting fraction digits for: $currencyCode")
        return defaultFormatter.getFractionDigits(currencyCode)
            .onFailure { throwable ->
                val error = KurrencyError.FractionDigitsFailure(currencyCode, throwable)
                Cedar.tag("Kurrency").e(throwable, error.errorMessage)
            }
    }

    fun getFractionDigitsOrDefault(currencyCode: String): Int =
        getFractionDigits(currencyCode).getOrDefault(DEFAULT_FRACTION_DIGITS)

    fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> {
        return formatWithValidation(amount, currencyCode) {
            defaultFormatter.formatCurrencyStyle(it, currencyCode)
        }
    }

    fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String> {
        return formatWithValidation(amount, currencyCode) {
            defaultFormatter.formatIsoCurrencyStyle(it, currencyCode)
        }
    }
    
    private fun formatWithValidation(
        amount: String,
        currencyCode: String,
        format: (String) -> Result<String>
    ): Result<String> {
        if (!isValidCurrencyCode(currencyCode)) {
            val error = KurrencyError.InvalidCurrencyCode(currencyCode)
            Cedar.tag("Kurrency").w(error.errorMessage)
            return Result.failure(error)
        }
        
        if (!isValidAmount(amount)) {
            val error = KurrencyError.InvalidAmount(amount)
            Cedar.tag("Kurrency").w(error.errorMessage)
            return Result.failure(error)
        }
        
        Cedar.tag("Kurrency").d("Formatting: amount=$amount, currency=$currencyCode")
        return format(amount)
            .onFailure { throwable ->
                val error = KurrencyError.FormattingFailure(currencyCode, amount, throwable)
                Cedar.tag("Kurrency").e(throwable, error.errorMessage)
            }
    }
    
    private fun isValidCurrencyCode(code: String): Boolean = 
        code.length == 3 && code.all { it.isLetter() }
    
    private fun isValidAmount(amount: String): Boolean = 
        amount.isNotBlank() && amount.replaceCommaWithDot().toDoubleOrNull() != null
}

