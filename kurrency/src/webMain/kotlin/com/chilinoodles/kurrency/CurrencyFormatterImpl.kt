@file:OptIn(ExperimentalWasmJsInterop::class)

package com.chilinoodles.kurrency

import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import kotlin.js.ExperimentalWasmJsInterop

@JsFun("function(cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur}).resolvedOptions().maximumFractionDigits }")
private external fun jsGetMaxFractionDigits(cur: String): Int

@JsFun("function(amt, cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur}).format(+amt) }")
private external fun jsFormatSymbol(amt: String, cur: String): String

@JsFun("function(amt, cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur, currencyDisplay:'code'}).format(+amt) }")
private external fun jsFormatIso(amt: String, cur: String): String

actual class CurrencyFormatterImpl : CurrencyFormat {

    actual override fun getFractionDigits(currencyCode: String): Result<Int> {
        return runCatching {
            val fractionDigits = jsGetMaxFractionDigits(currencyCode)
            require(fractionDigits >= 0) { "Invalid fraction digits: $fractionDigits" }
            fractionDigits
        }
    }

    actual override fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String> {
        return runCatching {
            val normalizedAmount = amount.replaceCommaWithDot()
            val doubleValue = normalizedAmount.toDouble()
            require(doubleValue.isFinite()) { "Amount must be a finite number" }
            jsFormatSymbol(normalizedAmount, currencyCode)
        }
    }

    actual override fun formatIsoCurrencyStyle(
        amount: String,
        currencyCode: String
    ): Result<String> {
        return runCatching {
            val normalizedAmount = amount.replaceCommaWithDot()
            val doubleValue = normalizedAmount.toDouble()
            require(doubleValue.isFinite()) { "Amount must be a finite number" }
            jsFormatIso(normalizedAmount, currencyCode)
        }
    }
}

