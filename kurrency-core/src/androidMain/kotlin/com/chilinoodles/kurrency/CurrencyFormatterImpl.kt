package com.chilinoodles.kurrency

import android.icu.text.NumberFormat
import android.icu.util.Currency
import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import java.math.BigDecimal
import java.util.Locale

actual class CurrencyFormatterImpl actual constructor(kurrencyLocale: KurrencyLocale) : CurrencyFormat {

    private val platformLocale: Locale = kurrencyLocale.locale

    actual override fun getFractionDigits(currencyCode: String): Result<Int> =
        runCatching {
            val currency = Currency.getInstance(currencyCode.uppercase())
            val fractionDigits = currency.defaultFractionDigits
            require(fractionDigits >= 0) {
                "Unsupported fraction digits for currency: $currencyCode"
            }
            fractionDigits
        }

    actual override fun formatCurrencyStyle(
        amount: String,
        currencyCode: String
    ): Result<String> =
        format(amount, currencyCode, NumberFormat.CURRENCYSTYLE)

    actual override fun formatIsoCurrencyStyle(
        amount: String,
        currencyCode: String
    ): Result<String> =
        format(amount, currencyCode, NumberFormat.ISOCURRENCYSTYLE)

    private fun format(
        amount: String,
        currencyCode: String,
        style: Int
    ): Result<String> =
        runCatching {
            val currency = Currency.getInstance(currencyCode.uppercase())

            val normalized = amount.replaceCommaWithDot().trim()
            require(normalized.isNotEmpty()) {
                "Amount must not be blank"
            }

            val value = BigDecimal(normalized)

            val numberFormat = NumberFormat.getInstance(platformLocale, style).apply {
                this.currency = currency
                val fractionDigits = currency.defaultFractionDigits
                if (fractionDigits >= 0) {
                    minimumFractionDigits = fractionDigits
                    maximumFractionDigits = fractionDigits
                }
            }

            numberFormat.format(value)
        }
}

actual fun isValidCurrency(currencyCode: String): Boolean {
    if (currencyCode.length != 3 || !currencyCode.all { it.isLetter() }) {
        return false
    }

    val upperCode = currencyCode.uppercase()
    return runCatching {
        // Use Currency.getAvailableCurrencies() to get the valid currency codes
        val availableCurrencies = Currency.getAvailableCurrencies()
        availableCurrencies.any { it.currencyCode == upperCode }
    }.getOrDefault(false)
}
