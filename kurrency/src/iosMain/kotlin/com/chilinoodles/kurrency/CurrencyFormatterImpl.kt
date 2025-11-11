package com.chilinoodles.kurrency

import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyISOCodeStyle
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatterStyle
import platform.Foundation.currentLocale

actual class CurrencyFormatterImpl : CurrencyFormat {
    
    actual override fun getFractionDigits(currencyCode: String): Result<Int> {
        return runCatching {
            val formatter = NSNumberFormatter().apply {
                this.currencyCode = currencyCode
                this.numberStyle = NSNumberFormatterCurrencyStyle
            }
            val fractionDigits = formatter.maximumFractionDigits.toInt()
            require(fractionDigits >= 0) { "Invalid fraction digits: $fractionDigits" }
            fractionDigits
        }
    }
    
    actual override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, NSNumberFormatterCurrencyStyle)
    
    actual override fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String> =
        formatCurrency(amount, currencyCode, NSNumberFormatterCurrencyISOCodeStyle)
    
    private fun formatCurrency(
        amount: String,
        currencyCode: String,
        style: NSNumberFormatterStyle
    ): Result<String> {
        return runCatching {
            val doubleValue = amount.replaceCommaWithDot().toDouble()
            require(doubleValue.isFinite()) { "Amount must be a finite number" }
            
            val value = NSNumber(doubleValue)
            val numberFormatter = createNumberFormatter(currencyCode, style)
            numberFormatter.stringFromNumber(value) ?: ""
        }
    }
    
    private fun createNumberFormatter(
        currencyCode: String,
        style: NSNumberFormatterStyle
    ): NSNumberFormatter = NSNumberFormatter().apply {
        this.numberStyle = style
        this.locale = NSLocale.currentLocale
        this.currencyCode = currencyCode
    }
}

