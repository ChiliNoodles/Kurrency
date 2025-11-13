@file:OptIn(ExperimentalWasmJsInterop::class)

package com.chilinoodles.kurrency

import com.chilinoodles.kurrency.extensions.replaceCommaWithDot
import kotlin.js.ExperimentalWasmJsInterop

@JsFun("function(cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur}).resolvedOptions().maximumFractionDigits }")
private external fun jsGetMaxFractionDigits(cur: String): Int

@JsFun("function(cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur}).resolvedOptions().currency }")
private external fun jsGetResolvedCurrency(cur: String): String

@JsFun("function(amt, cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur}).format(+amt) }")
private external fun jsFormatSymbol(amt: String, cur: String): String

@JsFun("function(amt, cur) { return new Intl.NumberFormat(undefined, {style:'currency', currency:cur, currencyDisplay:'code'}).format(+amt) }")
private external fun jsFormatIso(amt: String, cur: String): String

@JsFun("function(cur) { try { if (typeof Intl.supportedValuesOf === 'function') { return Intl.supportedValuesOf('currency').includes(cur); } return null; } catch(e) { return null; } }")
private external fun jsIsSupportedCurrency(cur: String): Boolean?

@JsFun("function(cur) { try { new Intl.NumberFormat(undefined, {style:'currency', currency:cur}); return true; } catch(e) { return false; } }")
private external fun jsCanCreateCurrencyFormatter(cur: String): Boolean

actual class CurrencyFormatterImpl : CurrencyFormat {

    actual override fun getFractionDigits(currencyCode: String): Result<Int> {
        return runCatching {
            val upperCode = currencyCode.uppercase()
            val resolvedCurrency = jsGetResolvedCurrency(upperCode)
            require(resolvedCurrency == upperCode) { 
                "Invalid currency code: $currencyCode (resolved to: $resolvedCurrency)" 
            }
            val fractionDigits = jsGetMaxFractionDigits(upperCode)
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

actual fun isValidCurrency(currencyCode: String): Boolean =
    runCatching {
        val upperCode = currencyCode.uppercase()
        
        val isSupported = jsIsSupportedCurrency(upperCode)
        if (isSupported != null) {
            return isSupported
        }
        
        if (!jsCanCreateCurrencyFormatter(upperCode)) {
            return false
        }
        
        val resolvedCurrency = jsGetResolvedCurrency(upperCode)
        resolvedCurrency == upperCode
    }.getOrDefault(false)

