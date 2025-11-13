package com.chilinoodles.kurrency

import android.icu.text.NumberFormat
import android.icu.util.Currency
import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import java.math.BigDecimal
import java.util.Locale

actual class CurrencyFormatterImpl : CurrencyFormat {

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
            val locale = Locale.getDefault()
            val currency = Currency.getInstance(currencyCode.uppercase())

            val normalized = amount.replaceCommaWithDot().trim()
            require(normalized.isNotEmpty()) {
                "Amount must not be blank"
            }

            val value = BigDecimal(normalized)

            val numberFormat = NumberFormat.getInstance(locale, style).apply {
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

actual fun isValidCurrency(currencyCode: String): Boolean =
    runCatching {
        Currency.getInstance(currencyCode.uppercase())
        true
    }.getOrDefault(false)