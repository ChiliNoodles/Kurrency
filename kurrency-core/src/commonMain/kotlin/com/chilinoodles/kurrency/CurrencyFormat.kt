package com.chilinoodles.kurrency

interface CurrencyFormat {
    fun getFractionDigits(currencyCode: String): Result<Int>
    fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String>
    fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String>
}

