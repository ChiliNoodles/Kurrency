package com.chilinoodles.kurrency

import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

actual class CurrencyFormatterImpl actual constructor(
    kurrencyLocale: KurrencyLocale
) : CurrencyFormat {

    private val locale: Locale = kurrencyLocale.locale
    
    actual override fun getFractionDigits(currencyCode: String): Result<Int> {
        return runCatching {
            val currency = Currency.getInstance(currencyCode.uppercase())
            requireNotNull(currency) { "Currency instance is null for code: $currencyCode" }
            currency.defaultFractionDigits
        }
    }
    
    actual override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, useIsoCode = false)
    
    actual override fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, useIsoCode = true)
    
    private fun formatCurrency(
        amount: String,
        currencyCode: String,
        useIsoCode: Boolean
    ): Result<String> {
        return runCatching {
            val value = amount.replaceCommaWithDot().toDouble()
            require(value.isFinite()) { "Amount must be a finite number" }
            
            val currency = Currency.getInstance(currencyCode.uppercase())
            requireNotNull(currency) { "Currency instance is null for code: $currencyCode" }
            
            if (useIsoCode) {
                val numberFormat = NumberFormat.getNumberInstance(locale)
                numberFormat.minimumFractionDigits = currency.defaultFractionDigits
                numberFormat.maximumFractionDigits = currency.defaultFractionDigits
                "$currencyCode ${numberFormat.format(value)}"
            } else {
                val numberFormat = createNumberFormat(locale, currencyCode)
                numberFormat.format(value) ?: ""
            }
        }
    }
    
    private fun createNumberFormat(
        locale: Locale,
        currencyCode: String
    ): NumberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance(currencyCode)
    }
}

actual fun isValidCurrency(currencyCode: String): Boolean =
    runCatching {
        val currency = Currency.getInstance(currencyCode.uppercase())
        currency != null
    }.getOrDefault(false)
