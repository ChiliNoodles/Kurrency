
package org.kimplify.kurrency

import org.kimplify.cedar.logging.Cedar
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Kurrency private constructor(val code: String) {
    init {
        Cedar.tag("Kurrency").d("Currency created: code=$code")
    }

    /**
     * Gets the fraction digits for this currency.
     * Fraction digits are defined by ISO 4217 and do not vary by locale.
     *
     * @return Result containing the number of fraction digits, or failure if currency is invalid
     */
    val fractionDigits: Result<Int>
        get() = CurrencyFormatter.getFractionDigits(code)

    /**
     * Gets the fraction digits for this currency, or returns default value.
     *
     * @return The number of fraction digits, or 2 if there's an error
     */
    val fractionDigitsOrDefault: Int
        get() = CurrencyFormatter.getFractionDigitsOrDefault(code)

    companion object Companion {
        /**
         * Creates a Currency from a currency code.
         * Note: This validates the currency code exists before creating the Currency instance.
         *
         * @param code The ISO 4217 currency code (e.g., "USD", "EUR")
         * @return Result containing the Currency, or failure if the code is invalid
         */
        fun fromCode(code: String): Result<Kurrency> {
            return if (isValid(code)) {
                Result.success(Kurrency(code))
            } else {
                Result.failure(KurrencyError.InvalidCurrencyCode(code))
            }
        }

        fun isValid(code: String): Boolean {
            if (code.length != 3 || !code.all { it.isLetter() }) {
                return false
            }
            return isValidCurrency(code)
        }

        // Convenience properties for popular currencies
        // These provide easy access to commonly used currencies without having to construct them manually.
        // For any other ISO 4217 currency, use Currency.fromCode(code)

        /** US Dollar - World reserve currency */
        val USD: Kurrency get() = Kurrency("USD")

        /** Euro - Second most traded currency */
        val EUR: Kurrency get() = Kurrency("EUR")

        /** British Pound Sterling */
        val GBP: Kurrency get() = Kurrency("GBP")

        /** Japanese Yen */
        val JPY: Kurrency get() = Kurrency("JPY")

        /** Australian Dollar */
        val AUD: Kurrency get() = Kurrency("AUD")

        /** Canadian Dollar */
        val CAD: Kurrency get() = Kurrency("CAD")

        /** Swiss Franc */
        val CHF: Kurrency get() = Kurrency("CHF")

        /** Chinese Yuan */
        val CNY: Kurrency get() = Kurrency("CNY")

        /** Indian Rupee */
        val INR: Kurrency get() = Kurrency("INR")

        /** South Korean Won */
        val KRW: Kurrency get() = Kurrency("KRW")

        /** Mexican Peso */
        val MXN: Kurrency get() = Kurrency("MXN")

        /** Brazilian Real */
        val BRL: Kurrency get() = Kurrency("BRL")

        /** South African Rand */
        val ZAR: Kurrency get() = Kurrency("ZAR")

        /** New Zealand Dollar */
        val NZD: Kurrency get() = Kurrency("NZD")

        /** Singapore Dollar */
        val SGD: Kurrency get() = Kurrency("SGD")

        /** Hong Kong Dollar */
        val HKD: Kurrency get() = Kurrency("HKD")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Kurrency) return false
        return code == other.code
    }

    override fun hashCode(): Int = code.hashCode()

    override fun toString(): String = "Currency(code=$code)"
    
    fun formatAmount(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): Result<String> {
        Cedar.tag("Kurrency").d("Formatting amount: amount=$amount, currency=$code, style=$style")
        val formatter = CurrencyFormatter(locale)
        return when (style) {
            CurrencyStyle.Standard -> formatter.formatCurrencyStyleResult(amount, code)
            CurrencyStyle.Iso -> formatter.formatIsoCurrencyStyleResult(amount, code)
        }
    }
    
    fun formatAmount(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): Result<String> = formatAmount(amount.toString(), style, locale)

    fun formatAmountOrEmpty(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): String = formatAmount(amount, style, locale).getOrDefault("")

    fun formatAmountOrEmpty(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): String = formatAmount(amount, style, locale).getOrDefault("")

    fun format(
        amount: String,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): FormattedCurrencyDelegate = FormattedCurrencyDelegate(this, amount, style, locale)

    fun format(
        amount: Double,
        style: CurrencyStyle = CurrencyStyle.Standard,
        locale: KurrencyLocale = KurrencyLocale.systemLocale()
    ): FormattedCurrencyDelegate = FormattedCurrencyDelegate(this, amount.toString(), style, locale)
}

class FormattedCurrencyDelegate(
    private val currency: Kurrency,
    private val amount: String,
    private val style: CurrencyStyle,
    private val locale: KurrencyLocale
) : ReadOnlyProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return currency.formatAmount(amount, style, locale).getOrDefault("")
    }
}

enum class CurrencyStyle {
    Standard,
    Iso
}
