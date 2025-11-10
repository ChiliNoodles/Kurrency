package com.chilinoodles.kurrency

import android.icu.text.NumberFormat
import android.icu.util.Currency
import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import java.util.Locale

actual class CurrencyFormatterImpl : CurrencyFormat {
    
    actual override fun getFractionDigits(currencyCode: String): Result<Int> {
        return runCatching {
            val currency = java.util.Currency.getInstance(currencyCode)
            requireNotNull(currency) { "Currency instance is null for code: $currencyCode" }
            currency.defaultFractionDigits
        }
    }
    
    actual override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, NumberFormat.CURRENCYSTYLE)
    
    actual override fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, NumberFormat.ISOCURRENCYSTYLE)
    
    private fun formatCurrency(
        amount: String,
        currencyCode: String,
        style: Int
    ): Result<String> {
        return runCatching {
            val locale = Locale.getDefault()
            val value = amount.replaceCommaWithDot().toDouble()
            require(value.isFinite()) { "Amount must be a finite number" }
            
            val numberFormat = createNumberFormat(locale, style, currencyCode)
            numberFormat.format(value) ?: ""
        }
    }
    
    private fun createNumberFormat(
        locale: Locale,
        style: Int,
        currencyCode: String
    ): NumberFormat = NumberFormat.getInstance(
        Locale(locale.language, locale.country),
        style
    ).apply {
        currency = Currency.getInstance(currencyCode)
    }
}

