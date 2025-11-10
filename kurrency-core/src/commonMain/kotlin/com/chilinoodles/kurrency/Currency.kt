package com.chilinoodles.kurrency

import com.chilinoodles.cedar.logging.Cedar
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class Currency(
    val code: String,
    val fractionDigits: Int = CurrencyFormatter.getFractionDigitsOrDefault(code)
) {
    init {
        Cedar.tag("Kurrency").d("Currency created: code=$code, fractionDigits=$fractionDigits")
    }
    
    fun formatAmount(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): Result<String> {
        Cedar.tag("Kurrency").d("Formatting amount: amount=$amount, currency=$code, style=$style")
        return when (style) {
            CurrencyStyle.Standard -> CurrencyFormatter.formatCurrencyStyle(amount, code)
            CurrencyStyle.Iso -> CurrencyFormatter.formatIsoCurrencyStyle(amount, code)
        }
    }
    
    fun formatAmount(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): Result<String> = formatAmount(amount.toString(), style)
    
    fun formatAmountOrEmpty(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): String = formatAmount(amount, style).getOrDefault("")
    
    fun formatAmountOrEmpty(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): String = formatAmount(amount, style).getOrDefault("")
    
    fun format(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): FormattedCurrencyDelegate = FormattedCurrencyDelegate(this, amount, style)
    
    fun format(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard
    ): FormattedCurrencyDelegate = FormattedCurrencyDelegate(this, amount.toString(), style)
}

class FormattedCurrencyDelegate(
    private val currency: Currency,
    private val amount: String,
    private val style: CurrencyStyle
) : ReadOnlyProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return currency.formatAmount(amount, style).getOrDefault("")
    }
}

enum class CurrencyStyle {
    Standard,
    Iso
}

enum class CurrencyDisplayContext {
    AudCentric,
    Neutral
}

fun Currency.formatForAudCentricApp(
    amount: String,
    forceIso: Boolean = false
): Result<String> {
    return if (forceIso) {
        formatAmount(amount, CurrencyStyle.Iso)
    } else {
        when (code) {
            "AUD" -> formatAmount(amount, CurrencyStyle.Standard)
            else -> formatAmount(amount, CurrencyStyle.Iso)
        }
    }
}

fun Currency.formatForAudCentricApp(
    amount: Double,
    forceIso: Boolean = false
): Result<String> = formatForAudCentricApp(amount.toString(), forceIso)

fun Currency.formatForAudCentricAppOrEmpty(
    amount: String,
    forceIso: Boolean = false
): String = formatForAudCentricApp(amount, forceIso).getOrDefault("")

fun Currency.formatForAudCentricAppOrEmpty(
    amount: Double,
    forceIso: Boolean = false
): String = formatForAudCentricApp(amount, forceIso).getOrDefault("")

