package org.kimplify.kurrency

import org.kimplify.cedar.logging.Cedar
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class Currency(
    val code: String,
    val fractionDigits: Int = CurrencyFormatter.getFractionDigitsOrDefault(code)
) {
    init {
        Cedar.tag("Kurrency").d("Currency created: code=$code, fractionDigits=$fractionDigits")
    }
    
    companion object {
        fun isValid(code: String): Boolean {
            if (code.length != 3 || !code.all { it.isLetter() }) {
                return false
            }
            return isValidCurrency(code)
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Currency) return false
        return code == other.code
    }
    
    override fun hashCode(): Int = code.hashCode()
    
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
