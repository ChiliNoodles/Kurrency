package org.kimplify.kurrency

sealed class KurrencyError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    
    class InvalidCurrencyCode(
        val code: String
    ) : KurrencyError("Invalid currency code: $code")
    
    class InvalidAmount(
        val amount: String
    ) : KurrencyError("Invalid amount: $amount")
    
    class FormattingFailure(
        val currencyCode: String,
        val amount: String,
        cause: Throwable
    ) : KurrencyError("Formatting failed for $currencyCode: $amount", cause)
    
    class FractionDigitsFailure(
        val currencyCode: String,
        cause: Throwable
    ) : KurrencyError("Failed to get fraction digits for $currencyCode", cause)
}

