package com.chilinoodles.kurrency

import com.chilinoodles.cedar.logging.Cedar
import com.chilinoodles.kurrency.extensions.replaceCommaWithDot

expect class CurrencyFormatterImpl() : CurrencyFormat {
    override fun getFractionDigits(currencyCode: String): Result<Int>
    override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String>
    override fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String>
}

object CurrencyFormatter {
    private const val DEFAULT_FRACTION_DIGITS = 2
    
    private val formatter: CurrencyFormat by lazy { 
        Cedar.tag("Kurrency").d("Initializing CurrencyFormatter")
        CurrencyFormatterImpl() 
    }
    
    fun getFractionDigits(currencyCode: String): Result<Int> {
        if (!isValidCurrencyCode(currencyCode)) {
            val error = KurrencyError.InvalidCurrencyCode(currencyCode)
            Cedar.tag("Kurrency").w(error.errorMessage)
            return Result.failure(error)
        }
        
        Cedar.tag("Kurrency").d("Getting fraction digits for: $currencyCode")
        return formatter.getFractionDigits(currencyCode)
            .onFailure { throwable ->
                val error = KurrencyError.FractionDigitsFailure(currencyCode, throwable)
                Cedar.tag("Kurrency").e(throwable, error.errorMessage)
            }
    }
    
    fun getFractionDigitsOrDefault(currencyCode: String): Int =
        getFractionDigits(currencyCode).getOrDefault(DEFAULT_FRACTION_DIGITS)
    
    fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> {
        return formatWithValidation(amount, currencyCode) { 
            formatter.formatCurrencyStyle(it, currencyCode)
        }
    }
    
    fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String> {
        return formatWithValidation(amount, currencyCode) { 
            formatter.formatIsoCurrencyStyle(it, currencyCode)
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

